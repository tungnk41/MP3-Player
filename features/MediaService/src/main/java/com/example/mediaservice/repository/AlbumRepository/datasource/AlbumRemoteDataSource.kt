package com.example.mediaservice.repository.AlbumRepository.datasource

import com.example.mediaservice.network.MediaApiInterface
import com.example.mediaservice.repository.AlbumRepository.AlbumDataSource
import com.example.mediaservice.repository.models.Album
import com.example.mediaservice.utils.DataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AlbumRemoteDataSource @Inject constructor(private val musicServiceApi: MediaApiInterface) :
    AlbumDataSource {
    override suspend fun findAll(): List<Album> = withContext(Dispatchers.IO){
        prepareAllAlbum()
    }

    private suspend fun prepareAllAlbum(): List<Album> {
        val albums = musicServiceApi.getAllAlbum()?.body() ?: listOf()

        albums.forEach {
            it.dataSource = DataSource.REMOTE
        }
        return albums
    }
}