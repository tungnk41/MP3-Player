package com.example.baseproject.ui.playing

import androidx.fragment.app.viewModels
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentPlayingBinding
import com.example.baseproject.navigation.AppNavigation
import com.example.core.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlayingFragment : BaseFragment<FragmentPlayingBinding,PlayingViewModel>(R.layout.fragment_playing) {

    @Inject
    lateinit var appNavigation: AppNavigation

    private val viewModel: PlayingViewModel by viewModels()
    override fun getVM(): PlayingViewModel = viewModel
}