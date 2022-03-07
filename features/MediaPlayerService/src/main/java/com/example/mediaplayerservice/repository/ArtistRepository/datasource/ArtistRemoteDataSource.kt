package com.example.musicplayer.repository.ArtistRepository.datasource

import com.example.mediaplayerservice.network.MediaApiInterface
import com.example.musicplayer.repository.ArtistRepository.ArtistDataSource
import com.example.musicplayer.repository.models.Artist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ArtistRemoteDataSource @Inject constructor(private val musicServiceApi: MediaApiInterface) : ArtistDataSource {
    override suspend fun getAllArtist(): List<Artist> = withContext(Dispatchers.IO){
         musicServiceApi.getAllArtist()?.body() ?: listOf()
    }
}