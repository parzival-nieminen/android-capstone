package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.network.models.*

interface Repository {

    fun selectById(id: Int): LiveData<Election?>

    fun selectFollowedById(id: Int): LiveData<Followed?>

    fun selectAll(): LiveData<List<Election>>

    fun selectFollowedAll(): LiveData<List<Election>>

    suspend fun insert(id: Int)

    suspend fun insertAll(vararg election: Election)

    suspend fun delete(id: Int)

    suspend fun getElections(): ElectionResponse

    suspend fun getVoterInfo(address: String, id: Int): VoterInfoResponse

    suspend fun getRepresentatives(address: String): RepresentativeResponse
}
