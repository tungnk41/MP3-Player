package com.example.mediaservice.repository.models

import android.os.Parcelable
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import com.example.mediaservice.extensions.displayIconUri
import com.example.mediaservice.extensions.flag
import com.example.mediaservice.extensions.id
import com.example.mediaservice.extensions.title
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Artist(
    val id: Long = -1,
    @SerializedName("name")
    val title: String = "",
    @SerializedName("image")
    val iconUri: String = ""
): Parcelable {

    fun toMediaMetadataCompat(): MediaMetadataCompat {
        val builder = MediaMetadataCompat.Builder()
        builder.id = id.toString()
        builder.title = title
        builder.displayIconUri = iconUri
        builder.flag = MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
        return builder.build()
    }
}