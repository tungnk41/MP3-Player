package com.example.mediaservice.extensions

import android.support.v4.media.MediaMetadataCompat
import com.google.android.exoplayer2.MediaMetadata

fun MediaMetadata.toMediaMetadataCompat()  : MediaMetadataCompat {
    val builder = MediaMetadataCompat.Builder()
    builder.title = title.toString()
    builder.artist = artist.toString()
    builder.album = albumTitle.toString()
    builder.albumArtUri = artworkUri.toString()
    builder.duration = extras?.getLong(MediaMetadataCompat.METADATA_KEY_DURATION) ?: 0

    //for Description content
    builder.displayTitle = title.toString()
    builder.displaySubtitle = artist.toString()
    builder.displayIconUri = artworkUri.toString()
    return builder.build()
}