package com.example.musicplayer.repository.SongRepository

import android.database.Cursor
import android.support.v4.media.MediaMetadataCompat
import com.example.mediaplayerservice.const.LOCAL_DATA
import com.example.musicplayer.di.LocalDataSource
import com.example.musicplayer.di.RemoteDataSource
import com.example.musicplayer.repository.models.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SongRepository @Inject constructor(@LocalDataSource private val localDataSource: SongDataSource,@RemoteDataSource private val remoteDataSource: SongDataSource) {


    suspend fun getAllSong(dataType : Int) : List<MediaMetadataCompat>{
        return withContext(Dispatchers.Default){
            if(dataType == LOCAL_DATA) {
                getLocalAllSong()
            } else {
                getRemoteAllSong()
            }.map { it.toMediaMetadataCompat() }
        }
    }

    suspend fun getAllSongFromAlbum(albumId: Long, dataType: Int): List<MediaMetadataCompat> {
        return withContext(Dispatchers.Default){
            if(dataType == LOCAL_DATA) {
                getLocalSongFromAlbum(albumId)
            } else {
                getRemoteSongFromAlbum(albumId)
            }
        }.map { it.toMediaMetadataCompat() }
    }

    suspend fun getAllSongFromArtist(artistId: Long,dataType: Int) : List<MediaMetadataCompat> {
        return withContext(Dispatchers.Default){
            if(dataType == LOCAL_DATA) {
                getLocalSongFromArtist(artistId)
            } else {
                getRemoteSongFromArtist(artistId)
            }
        }.map { it.toMediaMetadataCompat() }
    }

    suspend fun getAllSongFromGenre(genreId: Long,dataType: Int) : List<MediaMetadataCompat> {
        return withContext(Dispatchers.Default){
            if(dataType == LOCAL_DATA) {
                getLocalSongFromGenre(genreId)
            } else {
                getRemoteSongFromGenre(genreId)
            }
        }.map { it.toMediaMetadataCompat() }
    }

    private suspend fun getRemoteAllSong() : List<Song> = remoteDataSource.getAllSong()
    private suspend fun getLocalAllSong() : List<Song> = localDataSource.getAllSong()

    private suspend fun getRemoteSongFromAlbum(albumId: Long) : List<Song> = remoteDataSource.getSongFromAlbum(albumId)
    private suspend fun getLocalSongFromAlbum(albumId: Long) : List<Song> = localDataSource.getSongFromAlbum(albumId)

    private suspend fun getRemoteSongFromArtist(artistId: Long) : List<Song> = remoteDataSource.getSongFromArtist(artistId)
    private suspend fun getLocalSongFromArtist(artistId: Long) : List<Song> = localDataSource.getSongFromArtist(artistId)

    private suspend fun getRemoteSongFromGenre(genreId: Long) : List<Song> = remoteDataSource.getSongFromGenre(genreId)
    private suspend fun getLocalSongFromGenre(genreId: Long) : List<Song> = localDataSource.getSongFromGenre(genreId)

}