package com.example.mediaservice.repository.models

import android.os.Parcelable
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import com.example.mediaservice.extensions.*
import com.example.mediaservice.utils.DataSource
import com.example.mediaservice.utils.MediaType
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Album(
    val id: Long = 0,
    @SerializedName("name")
    val title: String = "",
    @SerializedName("image")
    val iconUri: String = "",
    val totalSong: String = ""
) : Parcelable {

    fun toMediaMetadataCompat(dataSource: Int = DataSource.LOCAL): MediaMetadataCompat {
        val builder = MediaMetadataCompat.Builder()
        builder.id = MediaIdExtra(id = id, mediaType = MediaType.TYPE_ALBUM, dataSource = dataSource).toString()
        builder.title = title
        builder.albumArtUri = iconUri
        builder.displayIconUri = iconUri
        builder.flag = MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
        return builder.build()
    }
}