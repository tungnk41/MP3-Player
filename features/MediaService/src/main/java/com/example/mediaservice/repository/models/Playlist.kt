package com.example.mediaservice.repository.models

import android.os.Parcelable
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mediaservice.extensions.*
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "playlist")
@Parcelize
data class Playlist(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String = "",
    val iconUri: String = "",
    val userId: Long
) : Parcelable {

    fun toMediaMetadataCompat(): MediaMetadataCompat {
        val builder = MediaMetadataCompat.Builder()
        builder.id = id.toString()
        builder.title = title
        builder.displayIconUri = iconUri
        builder.flag = MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
        return builder.build()
    }
}