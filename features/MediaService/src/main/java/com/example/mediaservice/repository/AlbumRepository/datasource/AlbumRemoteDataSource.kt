package com.example.musicplayer.repository.AlbumRepository.datasource

import android.content.Context
import com.example.mediaplayerservice.network.MediaApiInterface
import com.example.musicplayer.repository.AlbumRepository.AlbumDataSource
import com.example.musicplayer.repository.models.Album
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AlbumRemoteDataSource @Inject constructor(private val musicServiceApi: MediaApiInterface) : AlbumDataSource {
    override suspend fun getAllAlbum(): List<Album> = withContext(Dispatchers.IO){
        musicServiceApi.getAllAlbum()?.body() ?: listOf()
    }
}