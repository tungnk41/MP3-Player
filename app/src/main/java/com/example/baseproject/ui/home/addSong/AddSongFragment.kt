package com.example.baseproject.ui.home.addSong

import androidx.fragment.app.viewModels
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentAddSongBinding
import com.example.core.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddSongFragment: BaseFragment<FragmentAddSongBinding,AddSongViewModel>(R.layout.fragment_add_song) {

    private val viewModel by viewModels<AddSongViewModel>()
    override fun getVM(): AddSongViewModel = viewModel
}