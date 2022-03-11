package com.example.baseproject.ui.tabLocalMusic

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentLocalMusicBinding
import com.example.baseproject.model.MediaItemUI
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.ui.tabLocalMusic.adapter.MediaItemAdapter
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

    private val mAdapter: MediaItemAdapter by lazy {
        MediaItemAdapter(
            requireContext(),
            onClickListener = { position, isChecked ->

            })
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        viewModel.connect()

        binding.listMediaItem.setHasFixedSize(false)
        mAdapter.setHasStableIds(true)
        (binding.listMediaItem.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        binding.listMediaItem.layoutManager = GridLayoutManager(requireContext(),3)
        binding.listMediaItem.adapter = mAdapter

        viewModel.mediaItems.observe(this, Observer {
            mAdapter.submitList(it)
        })





    }

    override fun setOnClick() {
        super.setOnClick()

    }
}