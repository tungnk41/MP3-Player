package com.example.musicplayer.repository.models

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import com.example.mediaplayerservice.extensions.*
import kotlinx.android.parcel.Parcelize
import java.util.concurrent.TimeUnit

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