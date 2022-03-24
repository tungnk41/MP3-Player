package com.example.mediaservice.repository.models

import android.net.Uri
import android.os.Parcelable
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mediaservice.extensions.*
import com.example.mediaservice.utils.DataSource
import com.example.mediaservice.utils.MediaType
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "playlist")
@Parcelize
data class Playlist(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val title: String = "",
    val iconUri: String = "",
    val userId: Long
) : Parcelable {

    fun toBrowserMediaItem(parentMediaType: Int) : MediaBrowserCompat.MediaItem {
        val mediaDescriptionBuilder = MediaDescriptionCompat.Builder()
            .setMediaId(MediaIdExtra(id = id,parentMediaType = parentMediaType, mediaType = MediaType.TYPE_PLAYLIST, dataSource = DataSource.NONE).toString())
            .setTitle(title)
            .setIconUri(Uri.parse(iconUri))
        return MediaBrowserCompat.MediaItem(mediaDescriptionBuilder.build(),
            MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
        )
    }
}