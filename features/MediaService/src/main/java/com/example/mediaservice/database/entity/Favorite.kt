package com.example.mediaservice.database.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "favorite")
@Parcelize
data class Favorite(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val userId: Long,
    val songId: Long,
    val remoteTable: Int, //True = 1, False = 0
): Parcelable