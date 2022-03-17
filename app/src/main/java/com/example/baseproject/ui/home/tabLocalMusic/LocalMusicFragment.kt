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

    companion object {
        const val SONGS = 0
        const val ALBUMS = 1
        const val ARTISTS = 2
        const val GENRES = 3
    }

    @Inject
    lateinit var homeNavigation: HomeNavigation

    private val viewModel: LocalMusicViewModel by viewModels()
    override fun getVM(): LocalMusicViewModel = viewModel

    private val mAdapter: MediaItemHorizontalAdapter by lazy {
        MediaItemHorizontalAdapter(
            requireContext(),
            onClickListener = { position ->
                d("Position $position")
                if(position == SONGS){
                    val bundle = Bundle()
                    bundle.putParcelable("mediaIdExtra",viewModel.getCurrentMediaIdExtra(position))
                    homeNavigation.openLocalMusicScreenToSongScreen(bundle)
                }
                if(position == ALBUMS){
                    val bundle = Bundle()
                    bundle.putParcelable("mediaIdExtra",viewModel.getCurrentMediaIdExtra(position))
                    homeNavigation.openLocalMusicScreenToAlbumScreen(bundle)
                }
                if(position == ARTISTS){
                    val bundle = Bundle()
                    bundle.putParcelable("mediaIdExtra",viewModel.getCurrentMediaIdExtra(position))
                    homeNavigation.openLocalMusicScreenToArtistScreen(bundle)
                }
                if(position == GENRES){
                    val bundle = Bundle()
                    bundle.putParcelable("mediaIdExtra",viewModel.getCurrentMediaIdExtra(position))
                    homeNavigation.openLocalMusicScreenToGenreScreen(bundle)
                }
            })
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        binding.rvListMediaItem.setHasFixedSize(false)
        if(!mAdapter.hasObservers()){
            mAdapter.setHasStableIds(true)
        }
        (binding.rvListMediaItem.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        binding.rvListMediaItem.adapter = mAdapter

    }

    override fun bindingStateView() {
        super.bindingStateView()

        viewModel.mediaItems.observe(this, Observer {
            mAdapter.submitList(it)
        })
    }

    override fun setOnClick() {
        super.setOnClick()

    }
}