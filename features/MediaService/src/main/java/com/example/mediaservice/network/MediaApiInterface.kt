package com.example.mediaplayerservice.network

import com.example.musicplayer.repository.models.Album
import com.example.musicplayer.repository.models.Artist
import com.example.musicplayer.repository.models.Genre
import com.example.musicplayer.repository.models.Song
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface MediaApiInterface {

    @GET
    suspend fun getAllSong(@Url url: String = "https://6218ef9681d4074e859c7eb8.mockapi.io/api/v1/songs") : Response<List<Song>>

    @GET
    suspend fun getTopSong(@Url url: String = "https://6218ef9681d4074e859c7eb8.mockapi.io/api/v1/top") : Response<List<Song>>

    @GET
    suspend fun getAllAlbum(@Url url: String = "https://6215b273c9c6ebd3ce2f03ca.mockapi.io/albums") : Response<List<Album>>

    @GET
    suspend fun getAllArtist(@Url url: String = "https://621ed50b849220b1fca26484.mockapi.io/api/v1/artists") : Response<List<Artist>>

    @GET
    suspend fun getAllGenre(@Url url: String = "https://621f24a1311a70591401d374.mockapi.io/genrees") : Response<List<Genre>>
}