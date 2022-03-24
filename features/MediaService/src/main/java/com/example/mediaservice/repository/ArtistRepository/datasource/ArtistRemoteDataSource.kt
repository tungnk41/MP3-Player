package com.example.mediaservice.repository.ArtistRepository.datasource

import com.example.mediaservice.network.MediaApiInterface
import com.example.mediaservice.repository.ArtistRepository.ArtistDataSource
import com.example.mediaservice.repository.models.Album
import com.example.mediaservice.repository.models.Artist
import com.example.mediaservice.utils.DataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ArtistRemoteDataSource @Inject constructor(private val musicServiceApi: MediaApiInterface) :
    ArtistDataSource {
    override suspend fun findAll(): List<Artist> = withContext(Dispatchers.IO){
        prepareAllArtist()
    }

    private suspend fun prepareAllArtist(): List<Artist> {
        val artists = musicServiceApi.getAllArtist()?.body() ?: listOf()

        artists.forEach {
            it.dataSource = DataSource.REMOTE
        }
        return artists
    }
}