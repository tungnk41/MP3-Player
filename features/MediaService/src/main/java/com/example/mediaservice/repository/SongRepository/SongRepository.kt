package com.example.mediaservice.repository.SongRepository

import android.support.v4.media.MediaMetadataCompat
import com.example.mediaservice.module.LocalDataSource
import com.example.mediaservice.module.RemoteDataSource
import com.example.mediaservice.repository.models.Song
import com.example.mediaservice.utils.DataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class SongRepository @Inject constructor(@LocalDataSource private val localDataSource: SongDataSource, @RemoteDataSource private val remoteDataSource: SongDataSource) {

    suspend fun findAll(dataSource : Int) : List<MediaMetadataCompat>{
        return withContext(Dispatchers.Default){
            if(dataSource == DataSource.LOCAL) {
                findAllLocalData()
            } else {
                findAllRemoteData()
            }.map { it.toMediaMetadataCompat(dataSource) }
        }
    }

    suspend fun findAllByAlbumId(albumId: Long, dataSource: Int): List<MediaMetadataCompat> {
        return withContext(Dispatchers.Default){
            if(dataSource == DataSource.LOCAL) {
                findAllLocalDataByAlbumId(albumId)
            } else {
                findAllRemoteDataByAlbumId(albumId)
            }
        }.map { it.toMediaMetadataCompat(dataSource) }
    }

    suspend fun findAllByArtistId(artistId: Long,dataSource: Int) : List<MediaMetadataCompat> {
        return withContext(Dispatchers.Default){
            if(dataSource == DataSource.LOCAL) {
                findAllLocalDataByArtistId(artistId)
            } else {
                findAllRemoteDataByArtistId(artistId)
            }
        }.map { it.toMediaMetadataCompat(dataSource) }
    }

    suspend fun findAllByGenreId(genreId: Long,dataSource: Int) : List<MediaMetadataCompat> {
        return withContext(Dispatchers.Default){
            if(dataSource == DataSource.LOCAL) {
                findAllLocalDataByGenreId(genreId)
            } else {
                findAllRemoteDataByGenreId(genreId)
            }
        }.map { it.toMediaMetadataCompat(dataSource) }
    }

    suspend fun findAllByPlaylistId(playlistId : Long, dataSource: Int) : List<MediaMetadataCompat> {
        return withContext(Dispatchers.Default){
            if(dataSource == DataSource.LOCAL) {
                findAllLocalDataByPlaylistId(playlistId)
            } else {
                findAllRemoteDataByPlaylistId(playlistId)
            }
        }.map { it.toMediaMetadataCompat(dataSource) }
    }

    private suspend fun findAllRemoteData() : List<Song> = remoteDataSource.findAll()
    private suspend fun findAllLocalData() : List<Song> = localDataSource.findAll()

    private suspend fun findAllRemoteDataByAlbumId(albumId: Long) : List<Song> = remoteDataSource.findAllByAlbumId(albumId)
    private suspend fun findAllLocalDataByAlbumId(albumId: Long) : List<Song> = localDataSource.findAllByAlbumId(albumId)

    private suspend fun findAllRemoteDataByArtistId(artistId: Long) : List<Song> = remoteDataSource.findAllByArtistId(artistId)
    private suspend fun findAllLocalDataByArtistId(artistId: Long) : List<Song> = localDataSource.findAllByArtistId(artistId)

    private suspend fun findAllRemoteDataByGenreId(genreId: Long) : List<Song> = remoteDataSource.findAllByGenreId(genreId)
    private suspend fun findAllLocalDataByGenreId(genreId: Long) : List<Song> = localDataSource.findAllByGenreId(genreId)

    private suspend fun findAllRemoteDataByPlaylistId(playlistId: Long) : List<Song> = remoteDataSource.findAllByPlaylistId(playlistId)
    private suspend fun findAllLocalDataByPlaylistId(playlistId: Long) : List<Song> = localDataSource.findAllByPlaylistId(playlistId)

}