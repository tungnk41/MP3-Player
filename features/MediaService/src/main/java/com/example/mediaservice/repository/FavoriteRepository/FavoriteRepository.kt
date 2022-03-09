package com.example.mediaservice.repository.FavoriteRepository

import com.example.mediaservice.module.LocalDataSource
import com.example.mediaservice.module.RemoteDataSource
import com.example.mediaservice.repository.FavoriteRepository.datasource.FavoriteRemoteDataSource
import javax.inject.Inject

class FavoriteRepository @Inject constructor(@LocalDataSource favoriteLocalDataSource: FavoriteDataSource, @RemoteDataSource favoriteRemoteDataSource: FavoriteDataSource) {



}