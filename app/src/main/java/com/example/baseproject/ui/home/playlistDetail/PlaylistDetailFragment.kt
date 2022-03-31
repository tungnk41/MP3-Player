package com.example.baseproject.ui.home.playlistDetail

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.SimpleItemAnimator
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentPlaylistDetailBinding
import com.example.baseproject.navigation.HomeNavigation
import com.example.baseproject.ui.adapter.MediaItemVerticalAdapter
import com.example.core.base.BaseFragment
import com.example.mediaservice.repository.models.MediaIdExtra
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber.d
import javax.inject.Inject

@AndroidEntryPoint
class PlaylistDetailFragment: BaseFragment<FragmentPlaylistDetailBinding,PlaylistDetailViewModel>(R.layout.fragment_playlist_detail) {

    @Inject
    lateinit var homeNavigation: HomeNavigation

    private var mediaIdExtra : MediaIdExtra? = null
    private val viewModel by viewModels<PlaylistDetailViewModel>()
    override fun getVM(): PlaylistDetailViewModel = viewModel

    private val mAdapter: MediaItemVerticalAdapter by lazy {
        MediaItemVerticalAdapter(
            requireContext(),
            onClickListener = { position  ->
                viewModel.playAtIndex(position)
            })
    }

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        mediaIdExtra = args?.getParcelable<MediaIdExtra>("mediaIdExtra")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.mediaIdExtra = mediaIdExtra

    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        mediaIdExtra?.let { viewModel.connectToMediaService() }
        (binding.rvPlaylistSong.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        binding.rvPlaylistSong.adapter = mAdapter
    }

    override fun bindingStateView() {
        super.bindingStateView()

        viewModel.listSongPlaylist.observe(viewLifecycleOwner, Observer {
            mAdapter.submitList(it)

            if(it.isNotEmpty()){
                Glide.with(binding.root)
                    .load(it[0].iconUri)
                    .placeholder(R.drawable.ic_default_art_24)
                    .transform(CenterCrop())
                    .into(binding.imgPlaylistBanner)
            }
        })
    }

    override fun setOnClick() {
        super.setOnClick()

        binding.btnAddSong.setOnClickListener {
            val bundle = Bundle().apply {
                putParcelable("mediaIdExtra", viewModel.mediaIdExtra)
            }
            homeNavigation.openPlaylistDetailScreenToAddSongPlaylistScreen(bundle)
        }
    }

    override fun onDestroyView() {
        viewModel.disconnectToMediaService()
        binding.rvPlaylistSong.adapter = null
        super.onDestroyView()
    }
}