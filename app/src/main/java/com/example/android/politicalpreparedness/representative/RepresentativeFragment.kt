package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.google.android.gms.location.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.*

class RepresentativeFragment : Fragment() {

    companion object {
        const val REQUEST_LOCATION_PERMISSION = 42
    }

    private lateinit var binding: FragmentRepresentativeBinding
    private var location: Location? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private val locationRequest: LocationRequest
        get() {
            return LocationRequest.create().apply {
                interval = 10000
                fastestInterval = 5000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
        }
    val viewModel: RepresentativeViewModel by viewModel()

    override fun onStart() {
        super.onStart()
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (newLocation in locationResult.locations) {
                    Timber.i("NEW LOCATION $newLocation")
                    location = newLocation
                }
                stopLocationUpdates()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        stopLocationUpdates()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRepresentativeBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        checkLocationPermissions()

        binding.representativeList.adapter = RepresentativeListAdapter()

        binding.buttonSearch.setOnClickListener {
            hideKeyboard()
            Timber.i(viewModel.address.value?.toFormattedString())
            viewModel.address.value?.let { address -> viewModel.callGetRepresentative(address) }
        }

        binding.useMyLocationButton.setOnClickListener {
            hideKeyboard()
            if (location == null) {
                checkLocationPermissions()
            }
            if (location != null) {
                val address = geoCodeLocation(location!!)
                Timber.i(address.toFormattedString())
                viewModel.address.postValue(address)
                viewModel.callGetRepresentative(address)
            }
        }
        return binding.root
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
            }
        }
    }

    private fun checkLocationPermissions() {
        if (isPermissionGranted()) {
            Timber.i("LOCATION PERMISSIONS IS GRANTED")
            getLocation()
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }

    @Suppress("DEPRECATED_IDENTITY_EQUALS")
    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) === PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            Timber.i("CURRENT LOCATION: $location")
            this.location = location
        }
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    startLocationUpdates()
                }
            }
    }

    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
            .map { address ->
                Address(
                    address.thoroughfare, address.subThoroughfare, address.locality,
                    address.adminArea,
                    address.postalCode
                )
            }
            .first()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest, locationCallback,
            Looper.getMainLooper()
        )
    }
}
