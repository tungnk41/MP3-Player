package com.example.baseproject.ui.tabLocalMusic

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentLocalMusicBinding
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.navigation.HomeNavigation
import com.example.baseproject.ui.adapter.MediaBrowsableItemAdapter
import com.example.baseproject.ui.bottomController.BottomControllerFragment
import com.example.core.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber.d
import javax.inject.Inject

@AndroidEntryPoint
class LocalMusicFragment :
    BaseFragment<FragmentLocalMusicBinding, LocalMusicViewModel>(R.layout.fragment_local_music) {

    @Inject
    lateinit var homeNavigation: HomeNavigation

    private val viewModel: LocalMusicViewModel by viewModels()
    override fun getVM(): LocalMusicViewModel = viewModel

    private val mAdapter: MediaBrowsableItemAdapter by lazy {
        MediaBrowsableItemAdapter(
            requireContext(),
            onClickListener = { position ->
                d("Position $position")
                if(position == 0){
                    homeNavigation.openLocalMusicScreenToSongScreen()
                }
            })
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        viewModel.startLoadingData()

        binding.listMediaItem.setHasFixedSize(false)
        if(!mAdapter.hasObservers()){
            mAdapter.setHasStableIds(true)
        }
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