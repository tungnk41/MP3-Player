package com.example.mediaservice.repository.GenreRepository

import com.example.mediaservice.repository.models.Genre

interface GenreDataSource {
    suspend fun getAllGenre(): List<Genre>
}