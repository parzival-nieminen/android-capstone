package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.network.CivicsApiService
import com.example.android.politicalpreparedness.network.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class RepositoryImpl constructor(
    private val service: CivicsApiService,
    private val electionDao: ElectionDao
) : Repository {

    override fun selectById(id: Int): LiveData<Election?> =
        electionDao.selectById(id)

    override fun selectAll(): LiveData<List<Election>> =
        electionDao.selectAll()

    override suspend fun insert(election: Election) {
        Timber.i("INSERT: ${election.toString()}")
        withContext(Dispatchers.IO) {
            electionDao.insert(election)
        }
    }

    override suspend fun delete(election: Election) {
        Timber.i("DELETE: ${election.toString()}")
        withContext(Dispatchers.IO) {
            electionDao.delete(election)
        }
    }

    override suspend fun getElections(): ElectionResponse =
        withContext(Dispatchers.IO) {
            service.getElections()
        }

    override suspend fun getVoterInfo(address: String, id: Int): VoterInfoResponse =
        withContext(Dispatchers.IO) {
            service.getVoterInfo(address, id)
        }

    override suspend fun getRepresentatives(address: String): RepresentativeResponse =
        withContext(Dispatchers.IO) {
            service.getRepresentatives(address)
        }
}

