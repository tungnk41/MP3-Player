package com.example.baseproject.ui.home.tabOnlineMusic

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentOnlineMusicBinding
import com.example.baseproject.model.Banner
import com.example.baseproject.model.MediaItemUI
import com.example.baseproject.model.MediaOnlineItem
import com.example.baseproject.navigation.HomeNavigation
import com.example.baseproject.ui.adapter.BannerAdapter
import com.example.baseproject.ui.adapter.MediaItemHorizontalAdapter
import com.example.baseproject.ui.adapter.MediaOnlineSectionAdapter
import com.example.core.base.BaseFragment
import com.example.mediaservice.repository.models.MediaIdExtra
import com.example.mediaservice.utils.DataSource
import com.google.common.net.MediaType
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import timber.log.Timber.d
import javax.inject.Inject

@AndroidEntryPoint
class OnlineMusicFragment :
    BaseFragment<FragmentOnlineMusicBinding, OnlineMusicViewModel>(R.layout.fragment_online_music) {

    companion object {
        const val SONGS = 0
        const val ALBUMS = 1
        const val ARTISTS = 2
        const val GENRES = 3
    }

    @Inject
    lateinit var homeNavigation: HomeNavigation

    private val viewModel: OnlineMusicViewModel by viewModels()
    override fun getVM(): OnlineMusicViewModel = viewModel

    private val mAdapter: MediaOnlineSectionAdapter by lazy {
        MediaOnlineSectionAdapter(requireContext(),
            onExpandedClickListener = { parentPos ->
                d("onExpandedClickListener $parentPos")
                if(parentPos == SONGS) {
                    val bundle = Bundle()
                    bundle.putParcelable("mediaIdExtra",MediaIdExtra(mediaType = com.example.mediaservice.utils.MediaType.TYPE_ALL_SONGS, dataSource = DataSource.REMOTE))
                    homeNavigation.openOnlineMusicScreenToSongScreen(bundle)
                }
                else if(parentPos == ALBUMS) {
                    val bundle = Bundle()
                    bundle.putParcelable("mediaIdExtra",MediaIdExtra(mediaType = com.example.mediaservice.utils.MediaType.TYPE_ALL_ALBUMS, dataSource = DataSource.REMOTE))
                    homeNavigation.openOnlineMusicScreenToAlbumScreen(bundle)
                }
                else if(parentPos == ARTISTS) {
                    val bundle = Bundle()
                    bundle.putParcelable("mediaIdExtra",MediaIdExtra(mediaType = com.example.mediaservice.utils.MediaType.TYPE_ALL_ARTISTS, dataSource = DataSource.REMOTE))
                    homeNavigation.openOnlineMusicScreenToArtistScreen(bundle)
                }
                else if(parentPos == GENRES) {
                    val bundle = Bundle()
                    bundle.putParcelable("mediaIdExtra",MediaIdExtra(mediaType = com.example.mediaservice.utils.MediaType.TYPE_ALL_GENRES, dataSource = DataSource.REMOTE))
                    homeNavigation.openOnlineMusicScreenToGenreScreen(bundle)
                }
                else {
                    d("Invalid position $parentPos")
                }
            },
            onItemClickListener = { parentPos, childPos ->
                d("onItemClickListener $parentPos $childPos")
            })
    }

    private val bannerAdapter: BannerAdapter by lazy {
        BannerAdapter(requireContext())
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        binding.rvListMediaOnlineSection.setHasFixedSize(false)
        if(!mAdapter.hasObservers()){
            mAdapter.setHasStableIds(true)
        }
        (binding.rvListMediaOnlineSection.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        binding.rvListMediaOnlineSection.adapter = mAdapter

        binding.vpBanner.adapter = bannerAdapter
    }


    override fun setOnClick() {
        super.setOnClick()

    }

    override fun bindingStateView() {
        super.bindingStateView()

        viewModel.listBanner.observe(this, Observer {
            bannerAdapter.submitList(it)
        })

        viewModel.listMediaOnlineSection.observe(this, Observer {
            mAdapter.submitList(it)
        })
    }
}