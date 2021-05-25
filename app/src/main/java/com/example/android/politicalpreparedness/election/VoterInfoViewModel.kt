package com.example.android.politicalpreparedness.election

import androidx.lifecycle.*
import com.example.android.politicalpreparedness.database.Repository
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import kotlinx.coroutines.launch
import timber.log.Timber

class VoterInfoViewModel(private val repository: Repository, val election: Election) : ViewModel() {

    init {
        viewModelScope.launch {
            try {
                val address = if (election.division.state.isNotBlank())
                    "${election.division.country}, ${election.division.state}"
                else election.division.state
                val voterInfoResponse = repository.getVoterInfo(address, election.id)
                _voterInfo.value = voterInfoResponse
            } catch (exception: Exception) {
                Timber.e(exception)
                _voterInfo.value = VoterInfoResponse(election)
            }
        }
    }

    private val _voterInfo = MutableLiveData<VoterInfoResponse?>()
    val voterInfo: LiveData<VoterInfoResponse?>
        get() = _voterInfo

    private val _urlBrowser = MutableLiveData<String?>()
    val urlBrowser: LiveData<String?>
        get() = _urlBrowser

    val isFollow: LiveData<Boolean> =
        Transformations.map(repository.selectFollowedById(election.id)) { it != null }

    fun toggleFollowElection(election: Election, isFollow: Boolean) {
        viewModelScope.launch {
            if (isFollow)
                repository.delete(election.id)
            else
                repository.insert(election.id)
        }
    }

    fun openBrowser(url: String) {
        _urlBrowser.value = url
    }

    fun openBrowserSuccessful() {
        _urlBrowser.value = null
    }
}
