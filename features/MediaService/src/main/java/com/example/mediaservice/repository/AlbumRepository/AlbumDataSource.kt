package com.example.mediaservice.repository.AlbumRepository

import com.example.mediaservice.repository.models.Album

interface AlbumDataSource {
    suspend fun getAllAlbum() : List<Album>
}