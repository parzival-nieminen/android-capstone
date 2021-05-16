package com.example.android.politicalpreparedness.database

import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(election: Election)

    @Query("SELECT * FROM election_table")
    fun selectAll(): List<Election>

    @Query("SELECT * FROM election_table WHERE id == :id")
    fun selectById(id: Int): Election?

    @Delete
    fun delete(election: Election)

    @Query("DELETE FROM election_table")
    fun deleteAll()
}
