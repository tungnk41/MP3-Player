package com.example.mediaservice.utils

const val CLIENT_PACKAGE_NAME = "com.example.baseproject"

object MediaType {
    const val TYPE_NONE = 0
    const val TYPE_MEDIA_ROOT = 1
    const val TYPE_ALL_SONGS = 2
    const val TYPE_ALL_ALBUMS = 3
    const val TYPE_ALL_ARTISTS = 4
    const val TYPE_ALL_GENRES = 5
    const val TYPE_ALL_PLAYLISTS = 6
    const val TYPE_SONG = 7
    const val TYPE_ALBUM = 8
    const val TYPE_ARTIST = 9
    const val TYPE_GENRE = 10
    const val TYPE_PLAYLIST = 11
}

object DataSource {
    const val NONE = 0
    const val LOCAL = 1
    const val REMOTE = 2
}

const val NOTIFICATION_ID = 100

const val NOTIFICATION_CHANNEL_ID = "media_channel_01"
const val NETWORK_FAILURE = "NETWORK_FAILURE"

const val DATABASE_NAME = "mediaservice.db"


const val CMD_CREATE_PLAYLIST = "com.example.mediaservice.add.playlist"
const val KEY_PLAYLIST_TITLE = "playlist_title"

const val CMD_CLICK_FAVORITE = "com.example.mediaservice.click.favorite"
const val KEY_MEDIA_ID = "media_id"


