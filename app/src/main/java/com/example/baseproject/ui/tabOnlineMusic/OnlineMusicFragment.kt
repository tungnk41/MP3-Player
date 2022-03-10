package com.example.baseproject.ui.tabOnlineMusic

import androidx.fragment.app.viewModels
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentOnlineMusicBinding
import com.example.core.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnlineMusicFragment :
    BaseFragment<FragmentOnlineMusicBinding, OnlineMusicViewModel>(R.layout.fragment_online_music) {

    private val viewModel: OnlineMusicViewModel by viewModels()

    override fun getVM(): OnlineMusicViewModel = viewModel

}