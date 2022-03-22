package com.example.mediaservice.repository.FavoriteRepository.datasource

import com.example.mediaservice.database.dao.FavoriteDao
import com.example.mediaservice.repository.models.entity.Favorite
import com.example.mediaservice.repository.FavoriteRepository.FavoriteDataSource
import com.example.mediaservice.session.UserSessionInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FavoriteLocalDataSource @Inject constructor(private val favoriteDao: FavoriteDao, private val userSessionInfo: UserSessionInfo) : FavoriteDataSource {

    override suspend fun findAll() : List<Favorite> = withContext(Dispatchers.IO) {
        favoriteDao.findAll(userSessionInfo.userId,userSessionInfo.deviceId)
    }

    override suspend fun findBySongId(songId: Long): Favorite = withContext(Dispatchers.IO) {
        favoriteDao.findBySongId(userSessionInfo.userId,userSessionInfo.deviceId, songId)
    }

    override suspend fun insert(favorite: Favorite): Long = withContext(Dispatchers.IO) {
        favoriteDao.insert(favorite)
    }

    override suspend fun update(favorite: Favorite) = withContext(Dispatchers.IO){
        favoriteDao.update(favorite)
    }
}