package com.example.mediaservice.repository.ArtistRepository

import android.support.v4.media.MediaMetadataCompat
import com.example.mediaservice.utils.DataSource.LOCAL
import com.example.mediaservice.module.LocalDataSource
import com.example.mediaservice.module.RemoteDataSource
import com.example.mediaservice.repository.models.Artist
import com.example.mediaservice.utils.DataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ArtistRepository @Inject constructor(
    @LocalDataSource private val localDataSource: ArtistDataSource,
    @RemoteDataSource private val remoteDataSource: ArtistDataSource
) {
    suspend fun findAll(dataSource : Int) : List<MediaMetadataCompat>{
        return withContext(Dispatchers.Default){
            if(dataSource == DataSource.LOCAL) {
                findAllLocalData()
            } else {
                findAllRemoteData()
            }.map { it.toMediaMetadataCompat() }
        }
    }

    private suspend fun findAllRemoteData(): List<Artist> = remoteDataSource.findAll()
    private suspend fun findAllLocalData(): List<Artist> = localDataSource.findAll()
}