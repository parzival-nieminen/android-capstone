package com.example.android.politicalpreparedness.representative

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.Repository
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch
import timber.log.Timber

class RepresentativeViewModel constructor(private val repository: Repository) :
    ViewModel() {

    private val _representatives = MutableLiveData<List<Representative>>()

    val representative: LiveData<List<Representative>>
        get() = _representatives

    private val _loading = MutableLiveData<Boolean>()

    val loading: LiveData<Boolean>
        get() = _loading

    val address = MutableLiveData(Address("", "", "", "", ""))

    init {
        _loading.value = false
    }

    fun callGetRepresentative(address: Address) {
        _loading.postValue(true)
        viewModelScope.launch {
            try {
                val response =
                    repository.getRepresentatives(address = address.toFormattedString())
                _representatives.value =
                    response.offices.flatMap { office ->
                        office.getRepresentatives(response.officials)
                    }
            } catch (exception: Exception) {
                Timber.e(exception.message)
            }
            _loading.postValue(false)
        }
    }
}

