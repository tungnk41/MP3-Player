package com.example.mediaservice.repository.models

import android.os.Parcelable
import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
import android.support.v4.media.MediaMetadataCompat
import com.example.mediaservice.extensions.*
import com.google.gson.annotations.SerializedName
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
    @SerializedName("source")
    val mediaUri: String = "",
    @SerializedName("image")
    val iconUri: String = "",
    var duration: Long = 0,
    val favorite: Int = 0
) : Parcelable
{

    fun toMediaMetadataCompat() :  MediaMetadataCompat{
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

}