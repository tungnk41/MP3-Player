package com.example.mediaservice.repository.SongRepository.datasource

import com.example.mediaservice.network.MediaApiInterface
import com.example.mediaservice.repository.SongRepository.SongDataSource
import com.example.mediaservice.repository.models.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SongRemoteDataSource @Inject constructor(private val musicServiceApi: MediaApiInterface) :
    SongDataSource {
    override suspend fun findAll(): List<Song> = withContext(Dispatchers.IO){
        findAllWithArtist()
    }

    override suspend fun findAllByAlbumId(albumId: Long): List<Song> = withContext(Dispatchers.IO){
        musicServiceApi.getAllSong()?.body()?.filter { it.albumId == albumId } ?: listOf()
    }

    override suspend fun findAllByArtistId(artistId: Long): List<Song> = withContext(Dispatchers.IO){
        musicServiceApi.getAllSong()?.body()?.filter { it.artistId == artistId } ?: listOf()
    }

    override suspend fun findAllByGenreId(genreId: Long): List<Song> = withContext(Dispatchers.IO){
        musicServiceApi.getAllSong()?.body()?.filter { it.genreId == genreId } ?: listOf()
    }

    override suspend fun findAllByPlaylistId(playlistId: Long): List<Song> {
       return emptyList()
    }

    override suspend fun addSongToPlaylist(playlistId: Long, songId: Long) {

    }

    //#############################################

    private suspend fun findAllWithArtist(): List<Song> = withContext(Dispatchers.IO){
        val allSong = musicServiceApi.getAllSong()?.body() ?: listOf()
        val allArtist = musicServiceApi.getAllArtist()?.body() ?: listOf()

        val mapArtist = mutableMapOf<Long,String>()

        allArtist.forEach { artist ->
            mapArtist.put(artist.id,artist.title)
        }
        allSong.forEach { song ->
            song.artist = mapArtist.get(song.artistId).toString()
        }

        allSong
    }
}