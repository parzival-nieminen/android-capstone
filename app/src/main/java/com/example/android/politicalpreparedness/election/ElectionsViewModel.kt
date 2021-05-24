package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.Repository
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.launch

class ElectionsViewModel constructor(repository: Repository, app: Application) :
    AndroidViewModel(app) {

    init {
        viewModelScope.launch {
            try {
                val elections = repository.getElections()
                _upcomingElections.value = elections.elections
            } catch (exception: Exception) {
            }
        }
    }

    private val _goToVoterInfo = MutableLiveData<Election?>()
    val goToVoterInfo: LiveData<Election?>
        get() = _goToVoterInfo

    private val _upcomingElections = MutableLiveData<List<Election>>()
    val upcomingElections: LiveData<List<Election>>
        get() = _upcomingElections

    val selectedElections = repository.selectAll()

    fun displayVoterInfoSuccessful() {
        _goToVoterInfo.value = null
    }

    fun displayVoterInfo(election: Election) {
        _goToVoterInfo.value = election
    }
}
