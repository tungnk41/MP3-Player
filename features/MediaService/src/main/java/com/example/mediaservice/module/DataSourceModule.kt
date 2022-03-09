package com.example.mediaservice.module

import com.example.mediaservice.repository.AlbumRepository.AlbumDataSource
import com.example.mediaservice.repository.AlbumRepository.datasource.AlbumLocalDataSource
import com.example.mediaservice.repository.AlbumRepository.datasource.AlbumRemoteDataSource
import com.example.mediaservice.repository.ArtistRepository.ArtistDataSource
import com.example.mediaservice.repository.ArtistRepository.datasource.ArtistLocalDataSource
import com.example.mediaservice.repository.ArtistRepository.datasource.ArtistRemoteDataSource
import com.example.mediaservice.repository.FavoriteRepository.FavoriteDataSource
import com.example.mediaservice.repository.FavoriteRepository.datasource.FavoriteLocalDataSource
import com.example.mediaservice.repository.FavoriteRepository.datasource.FavoriteRemoteDataSource
import com.example.mediaservice.repository.GenreRepository.GenreDataSource
import com.example.mediaservice.repository.GenreRepository.datasource.GenreLocalDataSource
import com.example.mediaservice.repository.GenreRepository.datasource.GenreRemoteDataSource
import com.example.mediaservice.repository.PlaylistRepository.PlaylistDataSource
import com.example.mediaservice.repository.PlaylistRepository.datasource.PlaylistLocalDataSource
import com.example.mediaservice.repository.SongRepository.SongDataSource
import com.example.mediaservice.repository.SongRepository.datasource.SongLocalDataSource
import com.example.mediaservice.repository.SongRepository.datasource.SongRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LocalDataSource

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RemoteDataSource

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule{
    @LocalDataSource
    @Binds
    abstract fun bindSongLocalDataSource(localDataSource: SongLocalDataSource): SongDataSource

    @RemoteDataSource
    @Binds
    abstract fun bindSongRemoteDataSource(remoteDataSource: SongRemoteDataSource): SongDataSource

    @LocalDataSource
    @Binds
    abstract fun bindAlbumLocalDataSource(localDataSource: AlbumLocalDataSource): AlbumDataSource

    @RemoteDataSource
    @Binds
    abstract fun bindAlbumRemoteDataSource(remoteDataSource: AlbumRemoteDataSource): AlbumDataSource

    @LocalDataSource
    @Binds
    abstract fun bindArtistLocalDataSource(localDataSource: ArtistLocalDataSource): ArtistDataSource

    @RemoteDataSource
    @Binds
    abstract fun bindArtistRemoteDataSource(remoteDataSource: ArtistRemoteDataSource): ArtistDataSource

    @LocalDataSource
    @Binds
    abstract fun bindGenreLocalDataSource(localDataSource: GenreLocalDataSource): GenreDataSource

    @RemoteDataSource
    @Binds
    abstract fun bindGenreRemoteDataSource(remoteDataSource: GenreRemoteDataSource): GenreDataSource

    @LocalDataSource
    @Binds
    abstract fun bindPlaylistLocalDataSource(localDataSource: PlaylistLocalDataSource): PlaylistDataSource

    @RemoteDataSource
    @Binds
    abstract fun bindPlaylistRemoteDataSource(remoteDataSource: PlaylistLocalDataSource): PlaylistDataSource

    @LocalDataSource
    @Binds
    abstract fun bindFavoriteLocalDataSource(localDataSource: FavoriteLocalDataSource): FavoriteDataSource

    @RemoteDataSource
    @Binds
    abstract fun bindFavoriteRemoteDataSource(remoteDataSource: FavoriteRemoteDataSource): FavoriteDataSource
}