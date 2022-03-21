package com.example.mediaservice.repository.models

import android.os.Parcelable
import android.support.v4.media.MediaBrowserCompat
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
    val id: Long = 0,
    val title: String = "",
    val iconUri: String = "",
    val userId: Long
) : Parcelable {

    fun toMediaMetadataCompat(dataSource: Int = DataSource.NONE): MediaMetadataCompat {
        val builder = MediaMetadataCompat.Builder()
        builder.id = MediaIdExtra(id = id, mediaType = MediaType.TYPE_PLAYLIST, dataSource = dataSource).toString()
        builder.title = title
        builder.displayIconUri = iconUri
        builder.flag = MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
        return builder.build()
    }
}