package com.example.mediaservice.repository.models

import android.net.Uri
import android.os.Parcelable
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import com.example.mediaservice.extensions.displayIconUri
import com.example.mediaservice.extensions.flag
import com.example.mediaservice.extensions.id
import com.example.mediaservice.extensions.title
import com.example.mediaservice.utils.DataSource
import com.example.mediaservice.utils.MediaType
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Artist(
    val id: Long = 0,
    @SerializedName("name")
    val title: String = "",
    @SerializedName("image")
    val iconUri: String = "",
    var dataSource: Int
): Parcelable {

    fun toBrowserMediaItem(parentMediaType: Int) : MediaBrowserCompat.MediaItem {
        val mediaDescriptionBuilder = MediaDescriptionCompat.Builder()
            .setMediaId(MediaIdExtra(id = id,parentMediaType = parentMediaType, mediaType = MediaType.TYPE_SONG, dataSource = dataSource).toString())
            .setTitle(title)
            .setIconUri(Uri.parse(iconUri))
        return MediaBrowserCompat.MediaItem(mediaDescriptionBuilder.build(),
            MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
        )
    }
}