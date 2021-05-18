package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(election: Election)

    @Query("SELECT * FROM election_table ORDER BY electionDay ASC")
    fun selectAll(): LiveData<List<Election>>

    @Query("SELECT * FROM election_table WHERE id = :id")
    fun selectById(id: Int): Election

    @Query("DELETE FROM election_table WHERE id = :id")
    fun delete(id: Int)

    @Query("DELETE FROM election_table")
    fun deleteAll()
}