package com.example.baseproject.ui.home.createPlaylist

import androidx.fragment.app.viewModels
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentCreatePlaylistBinding
import com.example.core.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreatePlaylistFragment : BaseFragment<FragmentCreatePlaylistBinding,CreatePlaylistViewModel>(R.layout.fragment_create_playlist) {

    private val viewModel by viewModels<CreatePlaylistViewModel>()
    override fun getVM(): CreatePlaylistViewModel = viewModel


}