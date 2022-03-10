package com.example.baseproject.ui.tabLocalMusic

import androidx.fragment.app.viewModels
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentLocalMusicBinding
import com.example.baseproject.navigation.AppNavigation
import com.example.core.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LocalMusicFragment :
    BaseFragment<FragmentLocalMusicBinding, LocalMusicViewModel>(R.layout.fragment_local_music) {

    @Inject
    lateinit var appNavigation: AppNavigation

    private val viewModel: LocalMusicViewModel by viewModels()

    override fun getVM(): LocalMusicViewModel = viewModel

    override fun setOnClick() {
        super.setOnClick()

    }
}