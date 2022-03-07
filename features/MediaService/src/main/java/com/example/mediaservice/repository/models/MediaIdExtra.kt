package com.example.mediaservice.repository.models

import com.example.mediaservice.const.LOCAL_DATA
import com.google.gson.Gson

data class MediaIdExtra(var mediaType: Int? = null, var id: String? = null, var dataType : Int = LOCAL_DATA) {

    /*
    const val MEDIA_ID_ROOT = 0
    const val TYPE_ALL_SONGS = 1
    const val TYPE_ALL_ALBUMS = 2
    const val TYPE_ALL_ARTISTS = 3
    const val TYPE_ALL_GENRES = 4
    const val TYPE_ALL_PLAYLISTS = 5
    const val TYPE_SONG = 6
    const val TYPE_ALBUM = 7
    const val TYPE_ARTIST = 8
    const val TYPE_GENRE = 9
    const val TYPE_PLAYLIST = 10
    */

    fun getDataFromString(mediaIdExtra: String) : MediaIdExtra{
        return Gson().fromJson(mediaIdExtra,MediaIdExtra::class.java)
    }

    override fun toString(): String {
        return Gson().toJson(this)
    }
}