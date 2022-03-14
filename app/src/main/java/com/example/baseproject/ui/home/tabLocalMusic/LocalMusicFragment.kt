package com.example.baseproject.ui.home.tabLocalMusic

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentLocalMusicBinding
import com.example.baseproject.navigation.HomeNavigation
import com.example.baseproject.ui.adapter.MediaItemHorizontalAdapter
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

    private val mAdapter: MediaItemHorizontalAdapter by lazy {
        MediaItemHorizontalAdapter(
            requireContext(),
            onClickListener = { position ->
                d("Position $position")
                if(position == 0){
                    val bundle = Bundle()
                    bundle.putParcelable("mediaIdExtra",viewModel.getCurrentMediaIdExtra(position))
                    homeNavigation.openLocalMusicScreenToSongScreen(bundle)
                }
                if(position == 1){
                    val bundle = Bundle()
                    bundle.putParcelable("mediaIdExtra",viewModel.getCurrentMediaIdExtra(position))
                    homeNavigation.openLocalMusicScreenToAlbumScreen(bundle)
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