package com.example.mediaservice.repository.GenreRepository

import android.support.v4.media.MediaMetadataCompat
import com.example.mediaservice.const.LOCAL_DATA
import com.example.mediaservice.module.LocalDataSource
import com.example.mediaservice.module.RemoteDataSource
import com.example.mediaservice.repository.models.Genre
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GenreRepository @Inject constructor(
    @LocalDataSource private val localDataSource: GenreDataSource,
    @RemoteDataSource private val remoteDataSource: GenreDataSource
) {
    suspend fun getAllGenre(dataType : Int) : List<MediaMetadataCompat>{
        return withContext(Dispatchers.Default){
            if(dataType == LOCAL_DATA) {
                getLocalAllGenre()
            } else {
                getRemoteAllGenre()
            }.map { it.toMediaMetadataCompat() }
        }
    }

    private suspend fun getRemoteAllGenre(): List<Genre> = remoteDataSource.getAllGenre()
    private suspend fun getLocalAllGenre(): List<Genre> = localDataSource.getAllGenre()
}