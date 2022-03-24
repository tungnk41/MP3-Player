package com.example.mediaservice.repository.PlaylistRepository

import android.support.v4.media.MediaMetadataCompat
import com.example.mediaservice.utils.DataSource.LOCAL
import com.example.mediaservice.module.LocalDataSource
import com.example.mediaservice.module.RemoteDataSource
import com.example.mediaservice.repository.models.Playlist
import com.example.mediaservice.session.UserSessionInfo
import com.example.mediaservice.utils.DataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlaylistRepository @Inject constructor(
    @LocalDataSource private val localDataSource: PlaylistDataSource,
    @RemoteDataSource private val remoteDataSource: PlaylistDataSource,
    private val userSessionInfo: UserSessionInfo
    ) {
    suspend fun findAll(): List<Playlist> =
        withContext(Dispatchers.Default) {
            val listPlaylist = mutableListOf<List<Playlist>>()
            listPlaylist.add(findAllRemoteData(userSessionInfo.userId))
            listPlaylist.add(findAllLocalData(userSessionInfo.userId))
            listPlaylist.flatten()
        }

    suspend fun insert(playlist: Playlist) = withContext(Dispatchers.Default) {
        insertRemoteData(playlist)
        insertLocalData(playlist)
    }

    private suspend fun findAllRemoteData(userId: Long) : List<Playlist> = remoteDataSource.findAll(userId)
    private suspend fun findAllLocalData(userId: Long) : List<Playlist> = localDataSource.findAll(userId)

    private suspend fun insertRemoteData(playlist: Playlist) : Long = remoteDataSource.insert(playlist)
    private suspend fun insertLocalData(playlist: Playlist) : Long = localDataSource.insert(playlist)
}