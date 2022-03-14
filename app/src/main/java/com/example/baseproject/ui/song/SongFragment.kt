package com.example.baseproject.ui.song

import android.os.Bundle
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentSongBinding
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.navigation.HomeNavigation
import com.example.baseproject.ui.adapter.MediaPlayableItemAdapter
import com.example.core.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SongFragment: BaseFragment<FragmentSongBinding, SongViewModel>(R.layout.fragment_song) {

    @Inject
    lateinit var homeNavigation: HomeNavigation

    private val viewModel: SongViewModel by viewModels()
    override fun getVM(): SongViewModel = viewModel

    private val mAdapter: MediaPlayableItemAdapter by lazy {
        MediaPlayableItemAdapter(
            requireContext(),
            onClickListener = { position  ->
                viewModel.playAtIndex(position)
            })
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        viewModel.startLoadingData()

//        binding.listMediaItem.setHasFixedSize(false)
//        if(!mAdapter.hasObservers()){
//            mAdapter.setHasStableIds(true)
//        }
        (binding.listMediaItem.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        binding.listMediaItem.adapter = mAdapter

        viewModel.mediaItems.observe(this, Observer {
            mAdapter.submitList(it)
        })
    }

    override fun setOnClick() {
        super.setOnClick()

        binding.btnBack.setOnClickListener {
            homeNavigation.navController?.popBackStack()
        }
    }
}