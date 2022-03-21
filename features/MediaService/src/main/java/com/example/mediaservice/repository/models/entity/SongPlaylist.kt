package com.example.mediaservice.repository.models.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "song_playlist")
@Parcelize
data class SongPlaylist(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val songId: Long,
    val playlistId: Long,
    val remoteTable: Int,
    val position: Int,
): Parcelable