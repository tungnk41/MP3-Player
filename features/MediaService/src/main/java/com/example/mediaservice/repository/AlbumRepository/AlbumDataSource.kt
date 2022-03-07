package com.example.musicplayer.repository.AlbumRepository

import com.example.musicplayer.repository.models.Album

interface AlbumDataSource {

    suspend fun getAllAlbum() : List<Album>
}