package com.example.mediaservice.repository.SongRepository

import com.example.mediaservice.repository.models.Song

interface SongDataSource {
    suspend fun findAll() : List<Song>

    suspend fun findAllByAlbumId(albumId : Long) : List<Song>

    suspend fun findAllByArtistId(artistId : Long) : List<Song>

    suspend fun findAllByGenreId(genreId : Long) : List<Song>

    suspend fun findAllByPlaylistId(playlistId : Long) : List<Song>

    suspend fun addSongToPlaylist(playlistId: Long, songId: Long)
}