package com.example.musicplayer.repository.models


import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import com.example.mediaplayerservice.extensions.*
import com.google.android.exoplayer2.MediaMetadata
import kotlinx.android.parcel.Parcelize
import java.util.concurrent.TimeUnit

@Parcelize
data class Song(
    var id: Long = -1,
    val title: String = "",
    var album: String = "",
    val albumId: Long = -1,
    var artist: String = "",
    val artistId: Long = -1,
    var genre: String = "",
    val genreId: Long = -1,
    val source: String = "",
    val image: String = "",
    val duration: Long = 0,
) : Parcelable
{

    fun toMediaMetadataCompat() :  MediaMetadataCompat{
        val builder = MediaMetadataCompat.Builder()
        builder.id = id.toString()
        builder.title = title
        builder.artist = artist
        builder.album = album
        builder.genre = genre
        builder.mediaUri = source
        builder.albumArtUri = image
        builder.displayIconUri = image
        builder.duration = TimeUnit.SECONDS.toMillis(duration)
        builder.flag = FLAG_PLAYABLE
        return builder.build()
    }

}