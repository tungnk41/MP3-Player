package com.example.mediaservice.repository.models

import android.os.Parcelable
import com.example.mediaservice.utils.DataSource
import com.example.mediaservice.utils.DataSource.LOCAL
import com.google.gson.Gson
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MediaIdExtra(var parentMediaType: Int? = null, var mediaType: Int? = null, var id: Long? = null, var dataSource : Int = DataSource.LOCAL): Parcelable {

    /*
    const val MEDIA_ID_ROOT = 0
    const val MediaType.TYPE_ALL_SONGS = 1
    const val MediaType.TYPE_ALL_ALBUMS = 2
    const val MediaType.TYPE_ALL_ARTISTS = 3
    const val MediaType.TYPE_ALL_GENRES = 4
    const val MediaType.TYPE_ALL_PLAYLISTS = 5
    const val MediaType.TYPE_SONG = 6
    const val MediaType.TYPE_ALBUM = 7
    const val MediaType.TYPE_ARTIST = 8
    const val MediaType.TYPE_GENRE = 9
    const val MediaType.TYPE_PLAYLIST = 10
    */

    companion object {
        fun getDataFromString(mediaIdExtra: String) : MediaIdExtra{
            return Gson().fromJson(mediaIdExtra,MediaIdExtra::class.java)
        }
    }

    override fun toString(): String {
        return Gson().toJson(this)
    }

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }
}