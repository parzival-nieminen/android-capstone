package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.Repository
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.utils.isNetworkAvailable
import kotlinx.coroutines.launch

class ElectionsViewModel constructor(repository: Repository, app: Application) :
    AndroidViewModel(app) {

    init {
        viewModelScope.launch {
            try {
                if(isNetworkAvailable(app)) {
                    val elections = repository.getElections()
                    _upcomingElections.value = elections.elections
                    repository.insertAll(*elections.elections.map { it }.toTypedArray())
                }
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

    val offlineElection = repository.selectAll()
    val selectedElections = repository.selectFollowedAll()

    fun displayVoterInfoSuccessful() {
        _goToVoterInfo.value = null
    }

    fun displayVoterInfo(election: Election) {
        _goToVoterInfo.value = election
    }
}
