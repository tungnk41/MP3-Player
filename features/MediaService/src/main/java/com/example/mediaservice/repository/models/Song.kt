package com.example.mediaservice.repository.models

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import com.example.mediaservice.extensions.*
import com.example.mediaservice.utils.DataSource
import com.example.mediaservice.utils.MediaType
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.util.MimeTypes
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.concurrent.TimeUnit

@Parcelize
data class Song(
    val id: Long = 0,
    val title: String = "",
    var album: String = "",
    val albumId: Long = 0,
    var artist: String = "",
    val artistId: Long = 0,
    var genre: String = "",
    val genreId: Long = 0,
    @SerializedName("source")
    val mediaUri: String = "",
    @SerializedName("image")
    val iconUri: String = "",
    var duration: Long = 0,
    var favorite: Int = 0,
    var dataSource: Int,
) : Parcelable {

    fun toMediaMetadataCompat(): MediaMetadataCompat {
        val builder = MediaMetadataCompat.Builder()
        builder.id = id.toString()
        builder.title = title
        builder.artist = artist
        builder.album = album
        builder.genre = genre
        builder.mediaUri = mediaUri
        builder.albumArtUri = iconUri
        builder.displayIconUri = iconUri
        builder.duration = duration
        builder.favorite = favorite
        builder.flag = FLAG_PLAYABLE
        return builder.build()
    }

    fun toExoPlayerMetadata(): MediaMetadata {
        return with(MediaMetadata.Builder()) {
            setTitle(title)
            setArtist(artist)
            setAlbumTitle(album)
            setArtworkUri(Uri.parse(iconUri))
            val extras = Bundle()
            extras.putLong(MediaMetadataCompat.METADATA_KEY_DURATION, duration)
            setExtras(extras)
        }.build()
    }

    fun toExoPlayerMediaItem(): com.google.android.exoplayer2.MediaItem {
        return with(com.google.android.exoplayer2.MediaItem.Builder()) {
            setMediaId(id.toString())
            setUri(mediaUri)
            setMimeType(MimeTypes.AUDIO_MPEG)
            setMediaMetadata(toExoPlayerMetadata())
        }.build()
    }

    fun toBrowserMediaItem(parentMediaType: Int) : MediaBrowserCompat.MediaItem {
        val mediaDescriptionBuilder = MediaDescriptionCompat.Builder()
            .setMediaId(MediaIdExtra(id = id,parentMediaType = parentMediaType, mediaType = MediaType.TYPE_SONG, dataSource = dataSource).toString())
            .setTitle(title)
            .setSubtitle(artist)
            .setIconUri(Uri.parse(iconUri))
        return MediaBrowserCompat.MediaItem(mediaDescriptionBuilder.build(), FLAG_PLAYABLE)
    }
}