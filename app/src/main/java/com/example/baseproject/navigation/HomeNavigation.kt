package com.example.baseproject.navigation

import android.os.Bundle
import com.example.core.navigationComponent.BaseNavigator

interface HomeNavigation : BaseNavigator {

    fun openLocalMusicScreenToSongScreen(bundle: Bundle? = null)

    fun openLocalMusicScreenToAlbumScreen(bundle: Bundle? = null)

    fun openLocalMusicScreenToArtistScreen(bundle: Bundle? = null)

    fun openLocalMusicScreenToGenreScreen(bundle: Bundle? = null)

    fun openOnlineMusicScreenToSongScreen(bundle: Bundle? = null)

    fun openOnlineMusicScreenToAlbumScreen(bundle: Bundle? = null)

    fun openOnlineMusicScreenToArtistScreen(bundle: Bundle? = null)

    fun openOnlineMusicScreenToGenreScreen(bundle: Bundle? = null)

    fun openAlbumScreenToAlbumDetailScreen(bundle: Bundle? = null)

    fun openArtistScreenToArtistDetailScreen(bundle: Bundle? = null)

    fun openGenreScreenToGenreDetailScreen(bundle: Bundle? = null)

    fun openLocalMusicScreenToCreatePlaylistScreen(bundle: Bundle? = null)

    fun openCreatePlaylistScreenToPlaylistDetailScreen(bundle: Bundle? = null)

    fun openLocalMusicScreenToPlaylistDetailScreen(bundle: Bundle? = null)

    fun openOnlineMusicScreenToArtistDetailScreen(bundle: Bundle? = null)

    fun openOnlineMusicScreenToGenreDetailScreen(bundle: Bundle? = null)

    fun openOnlineMusicScreenToAlbumDetailScreen(bundle: Bundle? = null)

    fun openPlaylistDetailScreenToAddSongScreen(bundle: Bundle? = null)
}