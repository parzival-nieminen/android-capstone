package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.politicalpreparedness.network.CivicsApiService
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.State
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class RepositoryImpl constructor(
    private val service: CivicsApiService,
    private val electionDao: ElectionDao
) : Repository {

    override val state = MutableLiveData<State>()
    override val upcomingElections = MutableLiveData<List<Election>>()
    override val representatives = MutableLiveData<List<Representative>>()
    override val savedElections: LiveData<List<Election>> = electionDao.selectAll()

    override suspend fun reloadUpcomingElections() {
        Timber.i("call reloadUpcomingElections")
        withContext(Dispatchers.IO) {
            try {
                val electionResponse = service.getElections()
                upcomingElections.postValue(electionResponse.elections)
            } catch (exception: Exception) {
                Timber.e(exception)
            }
        }
    }

    override suspend fun reloadVoterInfo(address: String, electionId: Int) {
        Timber.i("call reloadVoterInfo")
        withContext(Dispatchers.IO) {
            try {
                val voterResponse = service.getVoterInfo(address, electionId)
                state.postValue(voterResponse.state?.get(0))
            } catch (exception: Exception) {
                Timber.e(exception)
            }
        }
    }

    override suspend fun reloadRepresentatives(address: String) {
        Timber.i("call reloadRepresentatives")
        withContext(Dispatchers.IO) {
            try {
                val (offices, officials) = service.getRepresentatives(address)
                representatives.postValue(offices.flatMap { office ->
                    office.getRepresentatives(
                        officials
                    )
                })
            } catch (exception: Exception) {
                Timber.e(exception)
            }
        }
    }

    override suspend fun followElection(election: Election) {
        Timber.i("call followElection")
        withContext(Dispatchers.IO) {
            electionDao.insert(election)
        }
    }

    override suspend fun unfollowElection(election: Election) {
        Timber.i("call isElectionSaved")
        withContext(Dispatchers.IO) {
            electionDao.delete(election.id)
        }
    }

    override suspend fun isElectionSaved(election: Election): Boolean {
        Timber.i("call isElectionSaved")
        val electionById: Election
        withContext(Dispatchers.IO) {
            electionById = electionDao.selectById(election.id)
        }
        return electionById == election
    }
}
