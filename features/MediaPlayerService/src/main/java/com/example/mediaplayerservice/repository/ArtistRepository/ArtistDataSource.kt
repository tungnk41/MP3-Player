package com.example.musicplayer.repository.ArtistRepository

import com.example.musicplayer.repository.models.Artist

interface ArtistDataSource {
    suspend fun getAllArtist(): List<Artist>
}