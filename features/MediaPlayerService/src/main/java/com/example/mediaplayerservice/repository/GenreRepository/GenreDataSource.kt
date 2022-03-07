package com.example.musicplayer.repository.GenreRepository

import com.example.musicplayer.repository.models.Genre

interface GenreDataSource {

    suspend fun getAllGenre(): List<Genre>
}