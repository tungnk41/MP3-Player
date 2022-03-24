package com.example.baseproject.ui.home.createPlaylist

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentCreatePlaylistBinding
import com.example.baseproject.navigation.HomeNavigation
import com.example.core.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CreatePlaylistFragment : BaseFragment<FragmentCreatePlaylistBinding,CreatePlaylistViewModel>(R.layout.fragment_create_playlist) {

    @Inject
    lateinit var homeNavigation: HomeNavigation

    private val viewModel by viewModels<CreatePlaylistViewModel>()
    override fun getVM(): CreatePlaylistViewModel = viewModel

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
    }

    override fun bindingAction() {
        super.bindingAction()

        viewModel.isLoading.observe(this, Observer {

        })
    }

    override fun bindingStateView() {
        super.bindingStateView()

        viewModel.createPlaylistCompleted.observe(this, Observer {
            if(it) {
                homeNavigation.openCreatePlaylistScreenToAddSongPlaylistScreen(Bundle()
                    .apply {
                        putString("title",viewModel.playlistTitle)
                        putParcelable("mediaIdExtra",viewModel.mediaIdExtra)
                    }
                )
            }
        })
    }
    override fun setOnClick() {
        super.setOnClick()
        binding.btnCreatePlaylist.setOnClickListener {
            viewModel.createPlaylist(binding.edtPlaylistName.text.toString())
        }
    }
}