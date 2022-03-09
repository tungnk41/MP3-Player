package com.example.mediaservice.repository.PlaylistRepository.datasource

import com.example.mediaservice.database.dao.PlayListDao
import com.example.mediaservice.repository.PlaylistRepository.PlaylistDataSource
import com.example.mediaservice.repository.models.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlaylistLocalDataSource @Inject constructor(private val playListDao: PlayListDao) : PlaylistDataSource{
    override suspend fun findAll(userId: Long): List<Playlist>  = withContext(Dispatchers.IO) {
        playListDao.findAll(userId)
    }

    override suspend fun insert(playlist: Playlist): Long  = withContext(Dispatchers.IO) {
        playListDao.insert(playlist)
    }

    override suspend fun update(playlist: Playlist) {
        playListDao.update(playlist)
    }

    override suspend fun delete(playlist: Playlist) {
        playListDao.delete(playlist)
    }
}