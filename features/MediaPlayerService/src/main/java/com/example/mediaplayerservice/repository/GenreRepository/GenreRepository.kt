package com.example.musicplayer.repository.GenreRepository

import android.support.v4.media.MediaMetadataCompat
import com.example.mediaplayerservice.const.LOCAL_DATA
import com.example.musicplayer.di.LocalDataSource
import com.example.musicplayer.di.RemoteDataSource
import com.example.musicplayer.repository.models.Genre
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