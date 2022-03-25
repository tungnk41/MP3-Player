package com.example.baseproject.ui.home.song

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentSongBinding
import com.example.baseproject.navigation.HomeNavigation
import com.example.baseproject.ui.adapter.MediaItemVerticalAdapter
import com.example.core.base.BaseFragment
import com.example.mediaservice.repository.models.MediaIdExtra
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber.d
import javax.inject.Inject

@AndroidEntryPoint
class SongFragment: BaseFragment<FragmentSongBinding, SongViewModel>(R.layout.fragment_song) {

    @Inject
    lateinit var homeNavigation: HomeNavigation

    private val viewModel: SongViewModel by viewModels()
    override fun getVM(): SongViewModel = viewModel

    private var parentMediaIdExtra: MediaIdExtra? = null

    private val mAdapter: MediaItemVerticalAdapter by lazy {
        MediaItemVerticalAdapter(
            requireContext(),
            onClickListener = { position  ->
                viewModel.playAtIndex(position)
            })
    }

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        parentMediaIdExtra = args?.getParcelable<MediaIdExtra>("mediaIdExtra")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        parentMediaIdExtra?.let { viewModel.startLoadingData(parentMediaIdExtra!!) }
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)


//        binding.listMediaItem.setHasFixedSize(false)
//        if(!mAdapter.hasObservers()){
//            mAdapter.setHasStableIds(true)
//        }
        (binding.rvListAllSong.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        binding.rvListAllSong.adapter = mAdapter

    }

    override fun setOnClick() {
        super.setOnClick()
    }

    override fun bindingStateView() {

        viewModel.mediaItems.observe(this, Observer {
            mAdapter.submitList(it)
        })
    }

    override fun onDestroyView() {
        binding.rvListAllSong.adapter = null
        super.onDestroyView()
    }
}