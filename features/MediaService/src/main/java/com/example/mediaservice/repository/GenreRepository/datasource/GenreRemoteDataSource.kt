package com.example.mediaservice.repository.GenreRepository.datasource

import com.example.mediaservice.network.MediaApiInterface
import com.example.mediaservice.repository.GenreRepository.GenreDataSource
import com.example.mediaservice.repository.models.Genre
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GenreRemoteDataSource @Inject constructor(private val musicServiceApi: MediaApiInterface):
    GenreDataSource {
    override suspend fun findAll(): List<Genre> = withContext(Dispatchers.IO){
        musicServiceApi.getAllGenre()?.body() ?: listOf()
    }
}