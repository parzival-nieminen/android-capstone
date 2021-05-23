package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {

    @Query("SELECT * FROM election_table WHERE id = :id")
    fun selectById(id: Int): LiveData<Election?>

    @Query("SELECT * FROM election_table ORDER BY electionDay")
    fun selectAll(): LiveData<List<Election>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(election: Election)

    @Delete
    fun delete(election: Election)

    @Query("DELETE FROM election_table")
    fun clearAll()
}
