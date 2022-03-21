package com.example.mediaservice.repository.PlaylistRepository.datasource

import com.example.mediaservice.repository.PlaylistRepository.PlaylistDataSource
import com.example.mediaservice.repository.models.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlaylistRemoteDataSource : PlaylistDataSource{
    override suspend fun findAll(userId: Long): List<Playlist> = withContext(Dispatchers.IO){
        emptyList()
    }

    override suspend fun insert(playlist: Playlist): Long = withContext(Dispatchers.IO){
        0
    }

    override suspend fun update(playlist: Playlist) = withContext(Dispatchers.IO) {
    }

    override suspend fun delete(playlist: Playlist) = withContext(Dispatchers.IO){
    }
}