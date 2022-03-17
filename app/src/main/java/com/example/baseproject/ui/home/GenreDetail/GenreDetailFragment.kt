package com.example.baseproject.ui.home.GenreDetail

import androidx.fragment.app.viewModels
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentGenreDetailBinding
import com.example.baseproject.navigation.HomeNavigation
import com.example.core.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GenreDetailFragment : BaseFragment<FragmentGenreDetailBinding,GenreDetailViewModel>(R.layout.fragment_genre_detail) {

    @Inject
    lateinit var homeNavigation: HomeNavigation

    private val viewModel by viewModels<GenreDetailViewModel>()
    override fun getVM(): GenreDetailViewModel = viewModel
}