package com.example.mediaservice.repository.ArtistRepository

import com.example.mediaservice.repository.models.Artist

interface ArtistDataSource {
    suspend fun getAllArtist(): List<Artist>
}