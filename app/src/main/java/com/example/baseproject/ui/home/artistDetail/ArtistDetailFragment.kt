package com.example.baseproject.ui.home.artistDetail

import androidx.fragment.app.viewModels
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentArtistDetailBinding
import com.example.baseproject.navigation.HomeNavigation
import com.example.baseproject.ui.home.artist.ArtistViewModel
import com.example.core.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ArtistDetailFragment: BaseFragment<FragmentArtistDetailBinding,ArtistDetailViewModel>(R.layout.fragment_artist_detail) {

    @Inject
    lateinit var homeNavigation: HomeNavigation

    private val viewModel by viewModels<ArtistDetailViewModel>()
    override fun getVM(): ArtistDetailViewModel = viewModel

}