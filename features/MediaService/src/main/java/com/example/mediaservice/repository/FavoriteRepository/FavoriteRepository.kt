package com.example.mediaservice.repository.FavoriteRepository

import com.example.mediaservice.module.LocalDataSource
import com.example.mediaservice.module.RemoteDataSource
import com.example.mediaservice.repository.models.entity.Favorite
import com.example.mediaservice.session.UserSessionInfo
import com.example.mediaservice.utils.DataSource
import javax.inject.Inject

class FavoriteRepository @Inject constructor(@LocalDataSource private val favoriteLocalDataSource: FavoriteDataSource, @RemoteDataSource private val favoriteRemoteDataSource: FavoriteDataSource) {

    suspend fun findAll() : List<Favorite> {
        val listFavorite = mutableListOf<List<Favorite>>()
        listFavorite.add(favoriteLocalDataSource.findAll())
        listFavorite.add(favoriteRemoteDataSource.findAll())
        return listFavorite.flatten()
    }

    suspend fun insert(favorite: Favorite, dataSource: Int) : Long {

        return if(dataSource == DataSource.REMOTE) favoriteRemoteDataSource.insert(favorite) else favoriteLocalDataSource.insert(favorite)
    }

    suspend fun update(favorite: Favorite,dataSource: Int) {
        if(dataSource == DataSource.REMOTE)
            favoriteRemoteDataSource.update(favorite)
        else
            favoriteLocalDataSource.update(favorite)
    }

}