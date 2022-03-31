package com.example.mediaservice.repository.SongRepository.datasource

import com.example.mediaservice.database.dao.SongPlaylistDao
import com.example.mediaservice.network.MediaApiInterface
import com.example.mediaservice.repository.SongRepository.SongDataSource
import com.example.mediaservice.repository.models.Song
import com.example.mediaservice.repository.models.entity.SongPlaylist
import com.example.mediaservice.utils.DataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SongRemoteDataSource @Inject constructor(private val musicServiceApi: MediaApiInterface,private val songPlaylistDao: SongPlaylistDao) :
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
        val setSongPlaylistId = songPlaylistDao.findAllByPlaylistId(playlistId).filter { it.isRemoteData }.map{ it.songId }.toHashSet()
        val listSong = findAll().filter { setSongPlaylistId.contains(it.id) }
        listSong
    }

    override suspend fun saveSongToPlaylist(playlistId: Long, songId: Long) {
        songPlaylistDao.insert(SongPlaylist(playlistId = playlistId, songId = songId, isRemoteData = true))
    }

    override suspend fun searchSongByTitle(title: String): List<Song> = withContext(Dispatchers.IO){
        val listSong = if(title.isNotEmpty()) findAll().filter { it.title.contains(title) } else findAll()
        listSong
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