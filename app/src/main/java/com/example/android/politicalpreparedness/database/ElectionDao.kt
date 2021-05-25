package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.Followed

@Dao
interface ElectionDao {

    @Query("SELECT * FROM election_table WHERE id = :id")
    fun selectById(id: Int): LiveData<Election?>

    @Query("SELECT * FROM followed_table WHERE electionId = :id")
    fun selectFollowedById(id: Int): LiveData<Followed?>

    @Query("SELECT * FROM election_table ORDER BY electionDay")
    fun selectAll(): LiveData<List<Election>>

    @Query("SELECT * FROM election_table WHERE id IN (SELECT electionId FROM followed_table) ORDER BY electionDay")
    fun selectFollowedAll(): LiveData<List<Election>>

    @Query("INSERT INTO followed_table(electionId) VALUES (:id)")
    fun insert(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg election: Election)

    @Query("DELETE FROM followed_table WHERE electionId = :id")
    fun delete(id: Int)

    @Query("DELETE FROM election_table")
    fun clearAll()
}
