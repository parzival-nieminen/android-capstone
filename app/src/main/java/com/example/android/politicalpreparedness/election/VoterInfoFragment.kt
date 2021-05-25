package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import com.example.android.politicalpreparedness.utils.hide
import com.example.android.politicalpreparedness.utils.show
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class VoterInfoFragment : Fragment() {

    private lateinit var binding: FragmentVoterInfoBinding
    private val args by navArgs<VoterInfoFragmentArgs>()
    val viewModel: VoterInfoViewModel by viewModel(
        clazz = VoterInfoViewModel::class,
        qualifier = null,
        parameters = { parametersOf(args.argElection) }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("VIEW TITLE: ${args.argElection.name}")
        (requireActivity() as AppCompatActivity).supportActionBar!!.title = args.argElection.name
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVoterInfoBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        viewModel.urlBrowser.observe(viewLifecycleOwner) { url ->
            if (!url.isNullOrEmpty()) {
                Timber.i("OPEN URL: $url")
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent)
                viewModel.openBrowserSuccessful()
            }
        }

        viewModel.voterInfo.observe(viewLifecycleOwner) { voterInfoResponse ->
            voterInfoResponse?.state?.stream()?.findFirst()?.get()
                ?.electionAdministrationBody?.let {
                    binding.electionAddress.hide()
                    binding.labelCorrespondenceHeader.hide()
                    if (it.correspondenceAddress != null) {
                        binding.electionAddress.text =
                            it.correspondenceAddress.toFormattedString()
                        binding.electionAddress.show()
                        binding.labelCorrespondenceHeader.show()
                    }
                    binding.votingLocation.hide()
                    if (it.votingLocationFinderUrl != null) {
                        binding.votingLocation.show()
                    }
                    binding.ballotInformation.hide()
                    if (it.ballotInfoUrl != null) {
                        binding.ballotInformation.show()
                    }
                }
        }
        return binding.root
    }
}
