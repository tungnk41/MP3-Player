package com.example.mediaservice.repository.FavoriteRepository.datasource

import com.example.mediaservice.repository.models.entity.Favorite
import com.example.mediaservice.repository.FavoriteRepository.FavoriteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FavoriteRemoteDataSource : FavoriteDataSource {
    override suspend fun findAll(userId: Long): List<Favorite> = withContext(Dispatchers.IO) {
        emptyList()
    }

    override suspend fun insert(favorite: Favorite): Long = withContext(Dispatchers.IO) {
        -1
    }

    override suspend fun update(favorite: Favorite) = withContext(Dispatchers.IO) {

    }
}