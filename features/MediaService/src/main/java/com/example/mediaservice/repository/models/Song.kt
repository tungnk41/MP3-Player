package com.example.mediaservice.repository.models

import android.os.Parcelable
import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
import android.support.v4.media.MediaMetadataCompat
import com.example.mediaservice.extensions.*
import kotlinx.android.parcel.Parcelize
import java.util.concurrent.TimeUnit

@Parcelize
data class Song(
    var _id: Long? = null, //This id is used for combine data between local and remote source
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