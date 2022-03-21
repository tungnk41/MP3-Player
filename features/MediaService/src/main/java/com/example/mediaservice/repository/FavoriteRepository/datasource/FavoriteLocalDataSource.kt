package com.example.mediaservice.repository.FavoriteRepository.datasource

import com.example.mediaservice.database.dao.FavoriteDao
import com.example.mediaservice.repository.models.entity.Favorite
import com.example.mediaservice.repository.FavoriteRepository.FavoriteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FavoriteLocalDataSource @Inject constructor(private val favoriteDao: FavoriteDao) : FavoriteDataSource {

    override suspend fun findAll(userId: Long) : List<Favorite> = withContext(Dispatchers.IO) {
            favoriteDao.findAllByUserId(userId)
    }

    override suspend fun insert(favorite: Favorite): Long = withContext(Dispatchers.IO) {
        favoriteDao.insert(favorite)
    }

    override suspend fun update(favorite: Favorite) = withContext(Dispatchers.IO){
        favoriteDao.update(favorite)
    }
}