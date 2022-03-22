package com.example.mediaservice.repository.models.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "favorite")
@Parcelize
data class Favorite(
    @PrimaryKey
    val songId: Long,
    val userId: Long,
    val deviceId: Long,
    var value: Int,
): Parcelable