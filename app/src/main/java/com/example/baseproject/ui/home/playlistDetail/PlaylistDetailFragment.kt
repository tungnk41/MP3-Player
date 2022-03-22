package com.example.baseproject.ui.home.playlistDetail

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentPlaylistDetailBinding
import com.example.core.base.BaseFragment
import com.example.mediaservice.repository.models.MediaIdExtra
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber.d

@AndroidEntryPoint
class PlaylistDetailFragment: BaseFragment<FragmentPlaylistDetailBinding,PlaylistDetailViewModel>(R.layout.fragment_playlist_detail) {

    private val viewModel by viewModels<PlaylistDetailViewModel>()
    override fun getVM(): PlaylistDetailViewModel = viewModel

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)

        d("PlaylistDetailFragment " + args?.getParcelable<MediaIdExtra>("mediaIdExtra").toString())
    }
}