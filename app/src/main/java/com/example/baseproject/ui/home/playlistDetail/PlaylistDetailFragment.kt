package com.example.baseproject.ui.home.playlistDetail

import androidx.fragment.app.viewModels
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentPlaylistDetailBinding
import com.example.core.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaylistDetailFragment: BaseFragment<FragmentPlaylistDetailBinding,PlaylistDetailViewModel>(R.layout.fragment_playlist_detail) {

    private val viewModel by viewModels<PlaylistDetailViewModel>()
    override fun getVM(): PlaylistDetailViewModel = viewModel
}