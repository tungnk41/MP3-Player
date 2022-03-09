package com.example.mediaservice.module

import com.example.mediaservice.repository.AlbumRepository.AlbumDataSource
import com.example.mediaservice.repository.AlbumRepository.AlbumRepository
import com.example.mediaservice.repository.ArtistRepository.ArtistDataSource
import com.example.mediaservice.repository.ArtistRepository.ArtistRepository
import com.example.mediaservice.repository.FavoriteRepository.FavoriteDataSource
import com.example.mediaservice.repository.FavoriteRepository.FavoriteRepository
import com.example.mediaservice.repository.GenreRepository.GenreDataSource
import com.example.mediaservice.repository.GenreRepository.GenreRepository
import com.example.mediaservice.repository.PlaylistRepository.PlaylistDataSource
import com.example.mediaservice.repository.PlaylistRepository.PlaylistRepository
import com.example.mediaservice.repository.SongRepository.SongDataSource
import com.example.mediaservice.repository.SongRepository.SongRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideSongRepository(
        @LocalDataSource localDataSource: SongDataSource,
        @RemoteDataSource remoteDataSource: SongDataSource
    ): SongRepository =
        SongRepository(localDataSource, remoteDataSource)

    @Provides
    @Singleton
    fun provideAlbumRepository(
        @LocalDataSource localDataSource: AlbumDataSource,
        @RemoteDataSource remoteDataSource: AlbumDataSource
    ): AlbumRepository =
        AlbumRepository(localDataSource, remoteDataSource)

    @Provides
    @Singleton
    fun provideArtistRepository(
        @LocalDataSource localDataSource: ArtistDataSource,
        @RemoteDataSource remoteDataSource: ArtistDataSource
    ): ArtistRepository =
        ArtistRepository(localDataSource, remoteDataSource)

    @Provides
    @Singleton
    fun provideGenreRepository(
        @LocalDataSource localDataSource: GenreDataSource,
        @RemoteDataSource remoteDataSource: GenreDataSource
    ): GenreRepository =
        GenreRepository(localDataSource, remoteDataSource)

    @Provides
    @Singleton
    fun providePlaylistRepository(
        @LocalDataSource localDataSource: PlaylistDataSource,
        @RemoteDataSource remoteDataSource: PlaylistDataSource
    ): PlaylistRepository =
        PlaylistRepository(localDataSource, remoteDataSource)

    @Provides
    @Singleton
    fun provideFavoriteRepository(
        @LocalDataSource localDataSource: FavoriteDataSource,
        @RemoteDataSource remoteDataSource: FavoriteDataSource
    ): FavoriteRepository =
        FavoriteRepository(localDataSource, remoteDataSource)
}