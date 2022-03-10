package com.example.mediaservice.repository.PlaylistRepository

import android.support.v4.media.MediaMetadataCompat
import com.example.mediaservice.utils.DataSource.LOCAL
import com.example.mediaservice.module.LocalDataSource
import com.example.mediaservice.module.RemoteDataSource
import com.example.mediaservice.repository.models.Playlist
import com.example.mediaservice.utils.DataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlaylistRepository @Inject constructor(@LocalDataSource private val localDataSource: PlaylistDataSource, @RemoteDataSource private val remoteDataSource: PlaylistDataSource) {
    suspend fun findAll(dataSource: Int,userId: Long) : List<MediaMetadataCompat> = withContext(Dispatchers.Default){
        if(dataSource == DataSource.LOCAL) {
             findAllLocalData(userId)
        }
        else {
             findAllRemoteData(userId)
        }.map { it.toMediaMetadataCompat() }
    }

    suspend fun insert(playlist: Playlist,dataSource: Int) = withContext(Dispatchers.Default) {
        if(dataSource == DataSource.LOCAL) {
            insertLocalData(playlist)
        }
        else {
            insertRemoteData(playlist)
        }
    }

    private suspend fun findAllRemoteData(userId: Long) : List<Playlist> = remoteDataSource.findAll(userId)
    private suspend fun findAllLocalData(userId: Long) : List<Playlist> = localDataSource.findAll(userId)

    private suspend fun insertRemoteData(playlist: Playlist) : Long = remoteDataSource.insert(playlist)
    private suspend fun insertLocalData(playlist: Playlist) : Long = localDataSource.insert(playlist)
}