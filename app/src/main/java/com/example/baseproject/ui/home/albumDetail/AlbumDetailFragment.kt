package com.example.baseproject.ui.home.albumDetail

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentAlbumDetailBinding
import com.example.baseproject.navigation.HomeNavigation
import com.example.baseproject.ui.adapter.MediaItemHorizontalAdapter
import com.example.baseproject.ui.adapter.MediaItemVerticalAdapter
import com.example.core.base.BaseFragment
import com.example.mediaservice.repository.models.MediaIdExtra
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class AlbumDetailFragment: BaseFragment<FragmentAlbumDetailBinding,AlbumDetailViewModel>(R.layout.fragment_album_detail) {

    @Inject
    lateinit var homeNavigation: HomeNavigation

    private val viewModel by viewModels<AlbumDetailViewModel>()
    override fun getVM(): AlbumDetailViewModel = viewModel

    private var parentMediaIdExtra: MediaIdExtra? = null
    private var title: String? = null

    private val mAdapter: MediaItemVerticalAdapter by lazy {
        MediaItemVerticalAdapter(
            requireContext(),
            onClickListener = { position ->
                Timber.d("Position $position")
                if(position == 0){

                }
            })
    }

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        parentMediaIdExtra = args?.getParcelable<MediaIdExtra>("mediaIdExtra")
        title = args?.getString("title")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentMediaIdExtra?.let {
            viewModel.startLoadingData(it)
            homeNavigation.navController?.currentDestination?.label = title
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        binding.rvListAlbumSong.setHasFixedSize(false)
        if(!mAdapter.hasObservers()){
            mAdapter.setHasStableIds(true)
        }
        (binding.rvListAlbumSong.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        binding.rvListAlbumSong.adapter = mAdapter

    }

    override fun bindingStateView() {
        super.bindingStateView()
        viewModel.mediaItems.observe(this, Observer {
            mAdapter.submitList(it)
        })
    }
}