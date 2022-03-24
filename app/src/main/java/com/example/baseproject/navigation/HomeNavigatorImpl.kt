package com.example.baseproject.navigation

import android.os.Bundle
import com.example.baseproject.R
import com.example.core.navigationComponent.BaseNavigatorImpl
import javax.inject.Inject

class HomeNavigatorImpl @Inject constructor() : BaseNavigatorImpl(), HomeNavigation {

    override fun openLocalMusicScreenToSongScreen(bundle: Bundle?) {
        openScreen(R.id.action_localMusicFragment_toSongFragment, bundle)
    }

    override fun openLocalMusicScreenToAlbumScreen(bundle: Bundle?) {
        openScreen(R.id.action_localMusicFragment_toAlbumFragment, bundle)
    }

    override fun openLocalMusicScreenToArtistScreen(bundle: Bundle?) {
        openScreen(R.id.action_localMusicFragment_toArtistFragment, bundle)
    }

    override fun openLocalMusicScreenToGenreScreen(bundle: Bundle?) {
        openScreen(R.id.action_localMusicFragment_toGenreFragment, bundle)
    }

    override fun openOnlineMusicScreenToSongScreen(bundle: Bundle?) {
        openScreen(R.id.action_onlineMusicFragment_toSongFragment, bundle)
    }

    override fun openOnlineMusicScreenToAlbumScreen(bundle: Bundle?) {
        openScreen(R.id.action_onlineMusicFragment_toAlbumFragment, bundle)
    }

    override fun openOnlineMusicScreenToArtistScreen(bundle: Bundle?) {
        openScreen(R.id.action_onlineMusicFragment_toArtistFragment, bundle)
    }

    override fun openOnlineMusicScreenToGenreScreen(bundle: Bundle?) {
        openScreen(R.id.action_onlineMusicFragment_toGenreFragment, bundle)
    }

    override fun openAlbumScreenToAlbumDetailScreen(bundle: Bundle?) {
        openScreen(R.id.action_albumFragment_toAlbumDetailFragment, bundle)
    }

    override fun openArtistScreenToArtistDetailScreen(bundle: Bundle?) {
        openScreen(R.id.action_artistFragment_toArtistDetailFragment, bundle)
    }

    override fun openGenreScreenToGenreDetailScreen(bundle: Bundle?) {
        openScreen(R.id.action_genreFragment_toGenreDetailFragment, bundle)
    }

    override fun openLocalMusicScreenToCreatePlaylistScreen(bundle: Bundle?) {
        openScreen(R.id.action_localMusicFragment_toCreatePlaylistFragment, bundle)
    }

    override fun openCreatePlaylistScreenToPlaylistDetailScreen(bundle: Bundle?) {
        openScreen(R.id.action_createPlaylistFragment_toPlaylistDetailFragment, bundle)
    }

    override fun openLocalMusicScreenToPlaylistDetailScreen(bundle: Bundle?) {
        openScreen(R.id.action_localMusicFragment_toPlaylistDetailFragment, bundle)
    }

    override fun openOnlineMusicScreenToArtistDetailScreen(bundle: Bundle?) {
        openScreen(R.id.action_onlineMusicFragment_toArtistDetailFragment, bundle)
    }

    override fun openOnlineMusicScreenToGenreDetailScreen(bundle: Bundle?) {
        openScreen(R.id.action_onlineMusicFragment_toGenreDetailFragment, bundle)
    }

    override fun openOnlineMusicScreenToAlbumDetailScreen(bundle: Bundle?) {
        openScreen(R.id.action_onlineMusicFragment_toAlbumDetailFragment, bundle)
    }

    override fun openPlaylistDetailScreenToAddSongScreen(bundle: Bundle?) {
        openScreen(R.id.action_playlistDetailFragment_toAddSongFragment, bundle)
    }


}