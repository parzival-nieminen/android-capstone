package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class ElectionsFragment : Fragment() {

    val viewModel: ElectionsViewModel by viewModel()
    private lateinit var binding: FragmentElectionBinding
    private lateinit var selectedElectionsAdapter: ElectionListAdapter
    private lateinit var upcomingElectionsAdapter: ElectionListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentElectionBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        selectedElectionsAdapter = ElectionListAdapter(getString(R.string.label_selected_elections)) {
            viewModel.displayVoterInfo(it)
        }

        binding.recyclerViewSelectedElection.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = selectedElectionsAdapter
            selectedElectionsAdapter.addHeaderAndSubmitList(listOf())
        }

        upcomingElectionsAdapter = ElectionListAdapter(getString(R.string.label_upcoming_elections)) {
            viewModel.displayVoterInfo(it)
        }

        binding.recyclerViewUpcomingElection.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = upcomingElectionsAdapter
            upcomingElectionsAdapter.addHeaderAndSubmitList(listOf())
        }

        viewModel.goToVoterInfo.observe(viewLifecycleOwner, Observer { election ->
            if (election != null) {
                findNavController().navigate(
                    ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(election)
                )
                viewModel.displayVoterInfoSuccessful()
            }
        })
        return binding.root
    }
}
