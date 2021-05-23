package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.ElectionResponse
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse

interface Repository {

    fun selectById(id: Int): LiveData<Election?>

    fun selectAll(): LiveData<List<Election>>

    suspend fun insert(election: Election)

    suspend fun delete(election: Election)

    suspend fun getElections(): ElectionResponse

    suspend fun getVoterInfo(address: String, id: Int): VoterInfoResponse

    suspend fun getRepresentatives(address: String): RepresentativeResponse
}
