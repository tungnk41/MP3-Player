package com.example.musicplayer.repository.SongRepository

import com.example.musicplayer.repository.models.Song


interface SongDataSource {
    suspend fun getAllSong() : List<Song>

    suspend fun getSongFromAlbum(albumId : Long) : List<Song>

    suspend fun getSongFromArtist(artistId : Long) : List<Song>

    suspend fun getSongFromGenre(genreId : Long) : List<Song>
}