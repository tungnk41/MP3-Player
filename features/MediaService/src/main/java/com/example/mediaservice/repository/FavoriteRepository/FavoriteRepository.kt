package com.example.mediaservice.repository.FavoriteRepository

import com.example.mediaservice.module.LocalDataSource
import com.example.mediaservice.module.RemoteDataSource
import com.example.mediaservice.repository.models.entity.Favorite
import javax.inject.Inject

class FavoriteRepository @Inject constructor(@LocalDataSource private val favoriteLocalDataSource: FavoriteDataSource, @RemoteDataSource private val favoriteRemoteDataSource: FavoriteDataSource) {

      suspend fun findAllByUserId(userId: Long) : List<Favorite> {
          val listFavorite = mutableListOf<List<Favorite>>()
          listFavorite.add(favoriteLocalDataSource.findAll(userId))
          listFavorite.add(favoriteRemoteDataSource.findAll(userId))
          return listFavorite.flatten()
      }

    suspend fun insert(favorite: Favorite) : Long {
        return if(favorite.isRemoteDataSource) favoriteRemoteDataSource.insert(favorite) else favoriteLocalDataSource.insert(favorite)
    }

    suspend fun update(favorite: Favorite) {
        if(favorite.isRemoteDataSource)
            favoriteRemoteDataSource.update(favorite)
        else
            favoriteLocalDataSource.update(favorite)
    }

}