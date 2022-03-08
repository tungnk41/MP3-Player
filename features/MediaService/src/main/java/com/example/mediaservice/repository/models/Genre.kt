package com.example.mediaservice.repository.models

import android.os.Parcelable
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import com.example.mediaservice.extensions.displayIconUri
import com.example.mediaservice.extensions.flag
import com.example.mediaservice.extensions.id
import com.example.mediaservice.extensions.title
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