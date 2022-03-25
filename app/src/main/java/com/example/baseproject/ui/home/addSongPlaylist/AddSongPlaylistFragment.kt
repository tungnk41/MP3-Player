package com.example.baseproject.ui.home.addSongPlaylist

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentAddSongPlaylistBinding
import com.example.baseproject.ui.adapter.AddSongPagerAdapter
import com.example.core.base.BaseFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber.d

@AndroidEntryPoint
class AddSongPlaylistFragment: BaseFragment<FragmentAddSongPlaylistBinding,AddSongPlaylistViewModel>(R.layout.fragment_add_song_playlist) {

    private val viewModel by viewModels<AddSongPlaylistViewModel>()
    override fun getVM(): AddSongPlaylistViewModel = viewModel
    private lateinit var pagerAdapter: AddSongPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pagerAdapter = AddSongPagerAdapter(childFragmentManager,lifecycle)
    }

    override fun bindingStateView() {
        super.bindingStateView()

        binding.vpViewPager.adapter = pagerAdapter

        binding.tlTabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
        TabLayoutMediator(binding.tlTabLayout,binding.vpViewPager) { tab, position ->
            when(position){
                0 -> tab.text = "Online"
                1 -> tab.text = "Local"
            }
        }.attach()
    }

    override fun onDestroyView() {
        binding.vpViewPager.adapter = null
        super.onDestroyView()
    }


}