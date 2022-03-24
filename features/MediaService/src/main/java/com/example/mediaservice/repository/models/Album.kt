package com.example.mediaservice.repository.models

import android.net.Uri
import android.os.Parcelable
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
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
    val totalSong: String = "",
    var dataSource: Int
) : Parcelable {

    fun toBrowserMediaItem(parentMediaType: Int) : MediaBrowserCompat.MediaItem {
        val mediaDescriptionBuilder = MediaDescriptionCompat.Builder()
            .setMediaId(MediaIdExtra(id = id,parentMediaType = parentMediaType, mediaType = MediaType.TYPE_ALBUM, dataSource = dataSource).toString())
            .setTitle(title)
            .setIconUri(Uri.parse(iconUri))
        return MediaBrowserCompat.MediaItem(mediaDescriptionBuilder.build(),
            MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
        )
    }
}