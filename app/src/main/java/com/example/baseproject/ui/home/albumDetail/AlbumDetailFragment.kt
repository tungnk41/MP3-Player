package com.example.baseproject.ui.home.albumDetail

import androidx.fragment.app.viewModels
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentAlbumDetailBinding
import com.example.baseproject.navigation.HomeNavigation
import com.example.core.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlbumDetailFragment: BaseFragment<FragmentAlbumDetailBinding,AlbumDetailViewModel>(R.layout.fragment_album_detail) {

    @Inject
    lateinit var homeNavigation: HomeNavigation

    private val viewModel by viewModels<AlbumDetailViewModel>()

    override fun getVM(): AlbumDetailViewModel = viewModel
}