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
import com.example.baseproject.navigation.HomeNavigation
import com.example.baseproject.ui.adapter.MediaItemHorizontalAdapter
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

    @Inject
    lateinit var homeNavigation: HomeNavigation

    private val viewModel: OnlineMusicViewModel by viewModels()
    override fun getVM(): OnlineMusicViewModel = viewModel

    private val mAdapter: MediaItemHorizontalAdapter by lazy {
        MediaItemHorizontalAdapter(
            requireContext(),
            onClickListener = { position ->

            })
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        binding.rvListAllSongCollapse.setHasFixedSize(false)
        if(!mAdapter.hasObservers()){
            mAdapter.setHasStableIds(true)
        }
        (binding.rvListAllSongCollapse.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        binding.rvListAllSongCollapse.adapter = mAdapter
    }

    override fun setOnClick() {
        super.setOnClick()

        binding.imgExpandedAllSong.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("mediaIdExtra",MediaIdExtra(mediaType = com.example.mediaservice.utils.MediaType.TYPE_ALL_SONGS, dataSource = DataSource.REMOTE))
            homeNavigation.openOnlineMusicScreenToSongcreen(bundle)
        }
    }

    override fun bindingStateView() {
        super.bindingStateView()

        viewModel.allSongsCollapse.observe(this, Observer {
            mAdapter.submitList(it)
        })
    }
}