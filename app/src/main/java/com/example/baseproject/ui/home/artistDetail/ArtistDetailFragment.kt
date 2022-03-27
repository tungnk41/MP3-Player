package com.example.baseproject.ui.home.artistDetail

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.SimpleItemAnimator
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentArtistDetailBinding
import com.example.baseproject.navigation.HomeNavigation
import com.example.baseproject.ui.adapter.MediaItemVerticalAdapter
import com.example.baseproject.ui.home.artist.ArtistViewModel
import com.example.core.base.BaseFragment
import com.example.mediaservice.repository.models.MediaIdExtra
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ArtistDetailFragment: BaseFragment<FragmentArtistDetailBinding,ArtistDetailViewModel>(R.layout.fragment_artist_detail) {

    @Inject
    lateinit var homeNavigation: HomeNavigation

    private val viewModel by viewModels<ArtistDetailViewModel>()
    override fun getVM(): ArtistDetailViewModel = viewModel

    private var parentMediaIdExtra: MediaIdExtra? = null

    private val mAdapter: MediaItemVerticalAdapter by lazy {
        MediaItemVerticalAdapter(
            requireContext(),
            onClickListener = { position ->
                viewModel.playAtIndex(position)
            })
    }

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        parentMediaIdExtra = args?.getParcelable<MediaIdExtra>("mediaIdExtra")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentMediaIdExtra?.let {
            viewModel.startLoadingData(it)
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        binding.rvListArtistSong.setHasFixedSize(false)
        if(!mAdapter.hasObservers()){
            mAdapter.setHasStableIds(true)
        }
        (binding.rvListArtistSong.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        binding.rvListArtistSong.adapter = mAdapter

    }

    override fun bindingStateView() {
        super.bindingStateView()
        viewModel.mediaItems.observe(viewLifecycleOwner, Observer {
            mAdapter.submitList(it)

            if(it.size > 0){
                Glide.with(binding.root)
                    .load(it[0].iconUri)
                    .placeholder(R.drawable.ic_default_art_24)
                    .transform(CenterCrop())
                    .into(binding.imgArtistBanner)
            }
        })
    }

    override fun onDestroyView() {
        binding.rvListArtistSong.adapter = null
        super.onDestroyView()
    }
}