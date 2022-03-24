package com.example.mediaservice.repository.models.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mediaservice.repository.models.Song
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "song_playlist")
@Parcelize
data class SongPlaylist(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    val songId: Long,
    val playlistId: Long,
): Parcelable