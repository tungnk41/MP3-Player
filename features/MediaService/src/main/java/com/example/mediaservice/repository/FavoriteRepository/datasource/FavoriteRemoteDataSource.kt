package com.example.mediaservice.repository.FavoriteRepository.datasource

import com.example.mediaservice.module.RemoteDataSource
import com.example.mediaservice.repository.models.entity.Favorite
import com.example.mediaservice.repository.FavoriteRepository.FavoriteDataSource
import com.example.mediaservice.session.UserSessionInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FavoriteRemoteDataSource @Inject constructor(private val userSessionInfo: UserSessionInfo) : FavoriteDataSource {
    override suspend fun findAll(): List<Favorite> = withContext(Dispatchers.IO) {
        emptyList()
    }

    override suspend fun findBySongId(songId: Long): Favorite? = withContext(Dispatchers.IO){
        null
    }

    override suspend fun insert(favorite: Favorite): Long = withContext(Dispatchers.IO) {
        -1
    }

    override suspend fun update(favorite: Favorite) = withContext(Dispatchers.IO) {

    }
}