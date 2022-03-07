package com.example.musicplayer.repository.GenreRepository.datasource

import com.example.mediaplayerservice.network.MediaApiInterface
import com.example.musicplayer.repository.GenreRepository.GenreDataSource
import com.example.musicplayer.repository.models.Genre
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GenreRemoteDataSource @Inject constructor(private val musicServiceApi: MediaApiInterface): GenreDataSource {
    override suspend fun getAllGenre(): List<Genre> = withContext(Dispatchers.IO){
        musicServiceApi.getAllGenre()?.body() ?: listOf()
    }
}