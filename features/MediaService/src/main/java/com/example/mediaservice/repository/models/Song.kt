package com.example.mediaservice.repository.models

import android.os.Parcelable
import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
import android.support.v4.media.MediaMetadataCompat
import com.example.mediaservice.extensions.*
import com.example.mediaservice.utils.DataSource
import com.example.mediaservice.utils.MediaType
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
    val duration: Long = 0,
    val favorite: Int = 0
) : Parcelable
{
    fun toMediaMetadataCompat(dataSource: Int) :  MediaMetadataCompat{
        val builder = MediaMetadataCompat.Builder()
        builder.id = MediaIdExtra(id = id, mediaType = MediaType.TYPE_SONG, dataSource = dataSource).toString()
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