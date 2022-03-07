package com.example.mediaservice.repository.SongRepository

import com.example.mediaservice.repository.models.Song

interface SongDataSource {
    suspend fun getAllSong() : List<Song>

    suspend fun getSongFromAlbum(albumId : Long) : List<Song>

    suspend fun getSongFromArtist(artistId : Long) : List<Song>

    suspend fun getSongFromGenre(genreId : Long) : List<Song>
}