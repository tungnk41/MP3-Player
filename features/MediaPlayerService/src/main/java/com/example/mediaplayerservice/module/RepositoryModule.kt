package com.example.musicplayer.di

import com.example.musicplayer.repository.AlbumRepository.AlbumDataSource
import com.example.musicplayer.repository.AlbumRepository.AlbumRepository
import com.example.musicplayer.repository.ArtistRepository.ArtistDataSource
import com.example.musicplayer.repository.ArtistRepository.ArtistRepository
import com.example.musicplayer.repository.GenreRepository.GenreDataSource
import com.example.musicplayer.repository.GenreRepository.GenreRepository
import com.example.musicplayer.repository.SongRepository.SongDataSource
import com.example.musicplayer.repository.SongRepository.SongRepository
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
}