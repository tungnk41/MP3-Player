package com.example.mediaservice.repository.PlaylistRepository.datasource

import com.example.mediaservice.repository.PlaylistRepository.PlaylistDataSource
import com.example.mediaservice.repository.models.Playlist

class PlaylistRemoteDataSource : PlaylistDataSource{
    override suspend fun findAll(userId: Long): List<Playlist> {
        return emptyList()
    }

    override suspend fun insert(playlist: Playlist): Long {
        return 0
    }

    override suspend fun update(playlist: Playlist) {
    }

    override suspend fun delete(playlist: Playlist) {
    }
}