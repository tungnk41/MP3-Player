package com.example.baseproject.ui.home.addSongPlaylist.listSongPager

import androidx.fragment.app.viewModels
import com.example.baseproject.R
import com.example.baseproject.databinding.FrgamentListSongPagerBinding
import com.example.core.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListSongPagerFragment : BaseFragment<FrgamentListSongPagerBinding,ListSongPagerViewModel>(R.layout.frgament_list_song_pager) {

    private val viewModel by viewModels<ListSongPagerViewModel>()
    override fun getVM(): ListSongPagerViewModel = viewModel
}