package com.example.baseproject.ui.home.addSongPlaylist

import androidx.fragment.app.viewModels
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentAddSongPlaylistBinding
import com.example.core.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddSongPlaylistFragment: BaseFragment<FragmentAddSongPlaylistBinding,AddSongPlaylistViewModel>(R.layout.fragment_add_song_playlist) {

    private val viewModel by viewModels<AddSongPlaylistViewModel>()
    override fun getVM(): AddSongPlaylistViewModel = viewModel
}