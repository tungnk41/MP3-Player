package com.example.mediaservice.repository.FavoriteRepository

import com.example.mediaservice.repository.models.entity.Favorite

interface FavoriteDataSource {
    suspend fun findAll() : List<Favorite>

    suspend fun findBySongId(songId: Long) : Favorite?

    suspend fun insert(favorite: Favorite): Long

    suspend fun update(favorite: Favorite)
}