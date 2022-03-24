package com.example.mediaservice.repository.AlbumRepository

import android.support.v4.media.MediaMetadataCompat
import com.example.mediaservice.utils.DataSource.LOCAL
import com.example.mediaservice.module.LocalDataSource
import com.example.mediaservice.module.RemoteDataSource
import com.example.mediaservice.repository.models.Album
import com.example.mediaservice.utils.DataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AlbumRepository @Inject constructor(
    @LocalDataSource private val localDataSource: AlbumDataSource,
    @RemoteDataSource private val remoteDataSource: AlbumDataSource
) {
    suspend fun findAll(dataSource : Int) : List<Album>{
        return withContext(Dispatchers.Default){
            if(dataSource == DataSource.LOCAL) {
                findAllLocalData()
            } else {
                findAllRemoteData()
            }
        }
    }

    private suspend fun findAllRemoteData(): List<Album> = remoteDataSource.findAll()
    private suspend fun findAllLocalData(): List<Album> = localDataSource.findAll()
}