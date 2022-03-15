package com.example.baseproject.ui.home.album

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentAlbumBinding
import com.example.baseproject.navigation.HomeNavigation
import com.example.baseproject.ui.adapter.MediaItemHorizontalAdapter
import com.example.core.base.BaseFragment
import com.example.mediaservice.repository.models.MediaIdExtra
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class AlbumFragment: BaseFragment<FragmentAlbumBinding,AlbumViewModel>(R.layout.fragment_album) {

    @Inject
    lateinit var homeNavigation: HomeNavigation

    private val viewModel: AlbumViewModel by viewModels()
    override fun getVM(): AlbumViewModel = viewModel

    private var parentMediaIdExtra: MediaIdExtra? = null

    private val mAdapter: MediaItemHorizontalAdapter by lazy {
        MediaItemHorizontalAdapter(
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
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        parentMediaIdExtra?.let {
            viewModel.startLoadingData(it)
        }


        binding.rvListAlbum.setHasFixedSize(false)
        if(!mAdapter.hasObservers()){
            mAdapter.setHasStableIds(true)
        }
        (binding.rvListAlbum.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        binding.rvListAlbum.adapter = mAdapter

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