package com.example.mediaservice.repository.FavoriteRepository

import com.example.mediaservice.database.entity.Favorite

interface FavoriteDataSource {
    suspend fun findAllFavorite(userId: Long) : List<Favorite>
}