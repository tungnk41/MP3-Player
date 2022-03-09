package com.example.mediaservice.repository.PlaylistRepository

import com.example.mediaservice.repository.models.Playlist

interface PlaylistDataSource {
    suspend fun findAll(userId: Long) : List<Playlist>

    suspend fun insert(playlist: Playlist): Long

    suspend fun update(playlist: Playlist)

    suspend fun delete(playlist: Playlist)
}