package com.example.baseproject.ui.home.addSongPlaylist.subScreenPager

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.baseproject.R
import com.example.baseproject.databinding.FrgamentListSongPagerBinding
import com.example.baseproject.ui.adapter.MediaItemVerticalAdapter
import com.example.baseproject.ui.home.addSongPlaylist.AddSongPlaylistFragment
import com.example.baseproject.ui.home.addSongPlaylist.AddSongPlaylistViewModel
import com.example.core.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import timber.log.Timber.d

@AndroidEntryPoint
class SubScreenPagerFragment(private val position: Int) : BaseFragment<FrgamentListSongPagerBinding,AddSongPlaylistViewModel>(R.layout.frgament_list_song_pager) {

    private val viewModel: AddSongPlaylistViewModel by activityViewModels()
    override fun getVM(): AddSongPlaylistViewModel = viewModel

    private val mAdapter: MediaItemVerticalAdapter by lazy {
        MediaItemVerticalAdapter(
            requireContext(),
            isTypeSelectButton = true,
            onClickListener = { position ->
                val currentItem = mAdapter.currentList[position]
                currentItem.isSelected = !currentItem.isSelected
                mAdapter.notifyItemChanged(position)
                viewModel.updateSelectedItem(currentItem.id,currentItem.dataSource,currentItem.isSelected)
            })
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        (binding.rvListSongPager.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        binding.rvListSongPager.adapter = mAdapter
    }

    override fun bindingStateView() {
        super.bindingStateView()

        when(position) {
            AddSongPlaylistFragment.TAB_ONLINE -> {
                viewModel.searchRemoteSong.observe(viewLifecycleOwner, Observer {
                    mAdapter.submitList(it)
                })
            }
            AddSongPlaylistFragment.TAB_LOCAL -> {
                viewModel.searchLocalSong.observe(viewLifecycleOwner, Observer {
                    mAdapter.submitList(it)
                })
            }
        }

        viewModel.listSelectedSong.observe(viewLifecycleOwner, Observer {
            mAdapter.notifyDataSetChanged()
        })
    }

    override fun onDestroyView() {
        binding.rvListSongPager.adapter = null
        super.onDestroyView()
    }


}