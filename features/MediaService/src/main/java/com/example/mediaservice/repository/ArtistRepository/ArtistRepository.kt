package com.example.musicplayer.repository.ArtistRepository

import android.support.v4.media.MediaMetadataCompat
import com.example.mediaplayerservice.const.LOCAL_DATA
import com.example.musicplayer.di.LocalDataSource
import com.example.musicplayer.di.RemoteDataSource
import com.example.musicplayer.repository.models.Artist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ArtistRepository @Inject constructor(
    @LocalDataSource private val localDataSource: ArtistDataSource,
    @RemoteDataSource private val remoteDataSource: ArtistDataSource
) {
    suspend fun getAllArtist(dataType : Int) : List<MediaMetadataCompat>{
        return withContext(Dispatchers.Default){
            if(dataType == LOCAL_DATA) {
                getLocalAllArtist()
            } else {
                getRemoteAllArtist()
            }.map { it.toMediaMetadataCompat() }
        }
    }

    private suspend fun getRemoteAllArtist(): List<Artist> = remoteDataSource.getAllArtist()
    private suspend fun getLocalAllArtist(): List<Artist> = localDataSource.getAllArtist()
}