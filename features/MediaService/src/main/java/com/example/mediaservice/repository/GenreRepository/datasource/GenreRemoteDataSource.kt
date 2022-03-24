package com.example.mediaservice.repository.GenreRepository.datasource

import com.example.mediaservice.network.MediaApiInterface
import com.example.mediaservice.repository.GenreRepository.GenreDataSource
import com.example.mediaservice.repository.models.Artist
import com.example.mediaservice.repository.models.Genre
import com.example.mediaservice.utils.DataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GenreRemoteDataSource @Inject constructor(private val musicServiceApi: MediaApiInterface):
    GenreDataSource {
    override suspend fun findAll(): List<Genre> = withContext(Dispatchers.IO){
        prepareAllGenre()
    }

    private suspend fun prepareAllGenre(): List<Genre> {
        val genres = musicServiceApi.getAllGenre()?.body() ?: listOf()

        genres.forEach {
            it.dataSource = DataSource.REMOTE
        }
        return genres
    }
}