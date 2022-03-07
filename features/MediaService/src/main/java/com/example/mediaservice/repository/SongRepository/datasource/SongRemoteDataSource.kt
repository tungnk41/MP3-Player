package com.example.mediaservice.repository.SongRepository.datasource

import com.example.mediaservice.network.MediaApiInterface
import com.example.mediaservice.repository.SongRepository.SongDataSource
import com.example.mediaservice.repository.models.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SongRemoteDataSource @Inject constructor(private val musicServiceApi: MediaApiInterface) :
    SongDataSource {
    override suspend fun getAllSong(): List<Song> = withContext(Dispatchers.IO){
        getAllSongHasArtist()
    }

    override suspend fun getSongFromAlbum(albumId: Long): List<Song> = withContext(Dispatchers.IO){
        musicServiceApi.getAllSong()?.body()?.filter { it.albumId == albumId } ?: listOf()
    }

    override suspend fun getSongFromArtist(artistId: Long): List<Song> = withContext(Dispatchers.IO){
        musicServiceApi.getAllSong()?.body()?.filter { it.artistId == artistId } ?: listOf()
    }

    override suspend fun getSongFromGenre(genreId: Long): List<Song> = withContext(Dispatchers.IO){
        musicServiceApi.getAllSong()?.body()?.filter { it.genreId == genreId } ?: listOf()
    }

    //#############################################

    private suspend fun getAllSongHasArtist(): List<Song> {
        val allSong = musicServiceApi.getAllSong()?.body() ?: listOf()
        val allArtist = musicServiceApi.getAllArtist()?.body() ?: listOf()

        val mapArtist = mutableMapOf<Long,String>()

        withContext(Dispatchers.Default) {
            allArtist.forEach { artist ->
                mapArtist.put(artist.id,artist.name)
            }

            allSong.forEach { song ->
                song.artist = mapArtist.get(song.artistId).toString()
            }
        }

        return allSong
    }
}