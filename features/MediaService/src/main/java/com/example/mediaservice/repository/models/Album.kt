package com.example.mediaservice.repository.models

import android.os.Parcelable
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import com.example.mediaservice.extensions.*
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Album(
    val id: Long = -1,
    val name: String = "",
    val image: String = "",
    val totalSong: String = ""
) : Parcelable {

    fun toMediaMetadataCompat(): MediaMetadataCompat {
        val builder = MediaMetadataCompat.Builder()
        builder.id = id.toString()
        builder.title = name
        builder.albumArtUri = image
        builder.displayIconUri = image
        builder.flag = MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
        return builder.build()
    }
}