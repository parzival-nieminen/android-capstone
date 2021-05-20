package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.State
import com.example.android.politicalpreparedness.representative.model.Representative

interface Repository {

    val state: MutableLiveData<State>
    val upcomingElections: MutableLiveData<List<Election>>
    val representatives: MutableLiveData<List<Representative>>
    val savedElections: LiveData<List<Election>>

    suspend fun reloadUpcomingElections()

    suspend fun reloadVoterInfo(address: String, electionId: Int)

    suspend fun reloadRepresentatives(address: String)

    suspend fun followElection(election: Election)

    suspend fun unfollowElection(election: Election)

    suspend fun isElectionSaved(election: Election): Boolean
}
