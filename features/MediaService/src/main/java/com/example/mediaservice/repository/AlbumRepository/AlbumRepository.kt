package com.example.mediaservice.repository.AlbumRepository

import android.support.v4.media.MediaMetadataCompat
import com.example.mediaservice.const.LOCAL_DATA
import com.example.mediaservice.module.LocalDataSource
import com.example.mediaservice.module.RemoteDataSource
import com.example.mediaservice.repository.models.Album
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AlbumRepository @Inject constructor(
    @LocalDataSource private val localDataSource: AlbumDataSource,
    @RemoteDataSource private val remoteDataSource: AlbumDataSource
) {
    suspend fun findAll(dataType : Int) : List<MediaMetadataCompat>{
        return withContext(Dispatchers.Default){
            if(dataType == LOCAL_DATA) {
                findAllLocalData()
            } else {
                findAllRemoteData()
            }.map { it.toMediaMetadataCompat() }
        }
    }

    private suspend fun findAllRemoteData(): List<Album> = remoteDataSource.findAll()
    private suspend fun findAllLocalData(): List<Album> = localDataSource.findAll()
}