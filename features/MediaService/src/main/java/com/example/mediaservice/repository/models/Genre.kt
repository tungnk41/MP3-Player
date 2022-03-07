package com.example.musicplayer.repository.models

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import com.example.mediaplayerservice.extensions.displayIconUri
import com.example.mediaplayerservice.extensions.flag
import com.example.mediaplayerservice.extensions.id
import com.example.mediaplayerservice.extensions.title
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Genre(
    val id: Long = 0,
    val name: String = "",
    val image: String = ""
): Parcelable {

    fun toMediaMetadataCompat(): MediaMetadataCompat {
        val builder = MediaMetadataCompat.Builder()
        builder.id = id.toString()
        builder.title = name
        builder.displayIconUri = image
        builder.flag = MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
        return builder.build()
    }
}