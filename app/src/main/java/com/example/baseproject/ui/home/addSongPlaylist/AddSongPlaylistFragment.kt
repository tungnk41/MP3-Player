package com.example.baseproject.ui.home.addSongPlaylist

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentAddSongPlaylistBinding
import com.example.baseproject.navigation.HomeNavigation
import com.example.baseproject.ui.adapter.AddSongPagerAdapter
import com.example.core.base.BaseFragment
import com.example.core.utils.onTextChange
import com.example.mediaservice.repository.models.MediaIdExtra
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber.d
import javax.inject.Inject

@AndroidEntryPoint
class AddSongPlaylistFragment: BaseFragment<FragmentAddSongPlaylistBinding,AddSongPlaylistViewModel>(R.layout.fragment_add_song_playlist) {

    companion object {
        const val TAB_ONLINE = 0
        const val TAB_LOCAL = 1
    }

    private var mediaIdExtra: MediaIdExtra? = null
    private val viewModel: AddSongPlaylistViewModel by activityViewModels()
    override fun getVM(): AddSongPlaylistViewModel = viewModel
    private lateinit var pagerAdapter: AddSongPagerAdapter
    private lateinit var tbTabLayoutMediator: TabLayoutMediator

    @Inject
    lateinit var homeNavigation: HomeNavigation

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        mediaIdExtra = args?.getParcelable("mediaIdExtra")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pagerAdapter = AddSongPagerAdapter(childFragmentManager,lifecycle)
        mediaIdExtra?.let {
            viewModel.mediaIdExtra = it
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

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
                TAB_ONLINE -> tab.text = "Online"
                TAB_LOCAL -> tab.text = "Local"
            }
        }.apply { tbTabLayoutMediator = this }.attach()

        binding.edtSearchText.addTextChangedListener{
            viewModel.searchSong(it.toString())
        }
    }

    override fun bindingStateView() {
        super.bindingStateView()
        viewModel.listSelectedSong.observe(viewLifecycleOwner, Observer {
            binding.groupButtonAction.isEnabled = it.isNotEmpty()

        })
    }

    override fun setOnClick() {
        super.setOnClick()
        binding.btnClear.setOnClickListener {
            viewModel.clearAllSelectedItem()
        }
        binding.btnDone.setOnClickListener {
            viewModel.saveSongToPlaylist()
            homeNavigation.navController?.popBackStack()
        }
    }

    override fun onDestroyView() {
        binding.vpViewPager.adapter = null
        tbTabLayoutMediator.detach()
        super.onDestroyView()
    }


}