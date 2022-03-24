package com.example.mediaservice.repository.SongRepository.datasource

import com.example.mediaservice.network.MediaApiInterface
import com.example.mediaservice.repository.SongRepository.SongDataSource
import com.example.mediaservice.repository.models.Song
import com.example.mediaservice.utils.DataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SongRemoteDataSource @Inject constructor(private val musicServiceApi: MediaApiInterface) :
    SongDataSource {
    override suspend fun findAll(): List<Song> = withContext(Dispatchers.IO){
        prepareAllSong()
    }

    override suspend fun findAllByAlbumId(albumId: Long): List<Song> = withContext(Dispatchers.IO){
        prepareAllSong().filter { it.albumId == albumId }
    }

    override suspend fun findAllByArtistId(artistId: Long): List<Song> = withContext(Dispatchers.IO){
        prepareAllSong().filter { it.artistId == artistId }
    }

    override suspend fun findAllByGenreId(genreId: Long): List<Song> = withContext(Dispatchers.IO){
        prepareAllSong().filter { it.genreId == genreId }
    }

    override suspend fun findAllByPlaylistId(playlistId: Long): List<Song> = withContext(Dispatchers.IO) {
        emptyList()
    }

    override suspend fun addSongToPlaylist(playlistId: Long, songId: Long) = withContext(Dispatchers.IO) {

    }

    //#############################################

    private suspend fun prepareAllSong() : List<Song> = withContext(Dispatchers.IO) {
        val allSong = musicServiceApi.getAllSong()?.body() ?: listOf()
        val allArtist = musicServiceApi.getAllArtist()?.body() ?: listOf()

        val mapArtist = mutableMapOf<Long,String>()

        allArtist.forEach { artist ->
            mapArtist.put(artist.id,artist.title)
        }
        allSong.forEach { song ->
            song.artist = mapArtist[song.artistId].toString()
            song.dataSource = DataSource.REMOTE
        }
        allSong
    }
}