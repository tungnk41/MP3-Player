package com.example.mediaservice.repository.GenreRepository

import com.example.mediaservice.module.LocalDataSource
import com.example.mediaservice.module.RemoteDataSource
import com.example.mediaservice.repository.models.Genre
import com.example.mediaservice.utils.DataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GenreRepository @Inject constructor(
    @LocalDataSource private val localDataSource: GenreDataSource,
    @RemoteDataSource private val remoteDataSource: GenreDataSource
) {
    suspend fun findAll(dataSource : Int) : List<Genre>{
        return withContext(Dispatchers.Default){
            if(dataSource == DataSource.LOCAL) {
                findAllLocalData()
            } else {
                findAllRemoteData()
            }
        }
    }

    private suspend fun findAllRemoteData(): List<Genre> = remoteDataSource.findAll()
    private suspend fun findAllLocalData(): List<Genre> = localDataSource.findAll()
}