package com.example.android.politicalpreparedness.network.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "followed_table")
data class Followed(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "electionId") val electionId: Int
) : Parcelable

