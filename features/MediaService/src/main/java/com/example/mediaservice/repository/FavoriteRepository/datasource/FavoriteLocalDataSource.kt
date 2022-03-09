package com.example.mediaservice.repository.FavoriteRepository.datasource

import com.example.mediaservice.database.dao.FavoriteDao
import com.example.mediaservice.database.entity.Favorite
import com.example.mediaservice.repository.FavoriteRepository.FavoriteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FavoriteLocalDataSource @Inject constructor(private val favoriteDao: FavoriteDao) : FavoriteDataSource {

    override suspend fun findAllFavorite(userId: Long) : List<Favorite> = withContext(Dispatchers.IO) {
            favoriteDao.findAllFavoriteByUserId(userId)
    }
}