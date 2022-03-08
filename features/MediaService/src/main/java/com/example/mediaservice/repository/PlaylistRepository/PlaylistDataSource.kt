package com.example.mediaservice.repository.PlaylistRepository

import com.example.mediaservice.repository.models.Playlist

interface PlaylistDataSource {
    suspend fun findAll() : List<Playlist>
}