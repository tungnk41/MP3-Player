package com.example.baseproject.ui.home.playlistDetail

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentPlaylistDetailBinding
import com.example.baseproject.navigation.HomeNavigation
import com.example.core.base.BaseFragment
import com.example.mediaservice.repository.models.MediaIdExtra
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber.d
import javax.inject.Inject

@AndroidEntryPoint
class PlaylistDetailFragment: BaseFragment<FragmentPlaylistDetailBinding,PlaylistDetailViewModel>(R.layout.fragment_playlist_detail) {

    @Inject
    lateinit var homeNavigation: HomeNavigation

    private val viewModel by viewModels<PlaylistDetailViewModel>()
    override fun getVM(): PlaylistDetailViewModel = viewModel

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        val mediaIdExtra = args?.getParcelable<MediaIdExtra>("mediaIdExtra")
        d("PlaylistDetailFragment " + mediaIdExtra.toString())
        viewModel.startLoadingAllSongInPlaylist(mediaIdExtra?.id ?: -1)
    }

    override fun bindingStateView() {
        super.bindingStateView()
    }

    override fun setOnClick() {
        super.setOnClick()

        binding.btnAddSong.setOnClickListener {
            homeNavigation.openPlaylistDetailScreenToAddSongScreen()
        }
    }
}