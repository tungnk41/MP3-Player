package com.example.mediaservice.repository.FavoriteRepository.datasource

import com.example.mediaservice.database.entity.Favorite
import com.example.mediaservice.repository.FavoriteRepository.FavoriteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FavoriteRemoteDataSource : FavoriteDataSource {
    override suspend fun findAllFavorite(userId: Long): List<Favorite> = withContext(Dispatchers.IO) {
        emptyList()
    }
}