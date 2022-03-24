package com.example.baseproject.ui.home.playlistDetail

import androidx.lifecycle.viewModelScope
import com.example.core.base.BaseViewModel
import com.example.mediaservice.repository.PlaylistRepository.PlaylistRepository
import com.example.mediaservice.repository.SongRepository.SongRepository
import com.example.mediaservice.utils.DataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber.d
import javax.inject.Inject

@HiltViewModel
class PlaylistDetailViewModel @Inject constructor(private val songRepository: SongRepository, private val playlistRepository: PlaylistRepository): BaseViewModel() {


    init {

    }

    fun startLoadingAllSongInPlaylist(playlistId: Long) {
        viewModelScope.launch {
            val songs = songRepository.findAllByPlaylistId(playlistId, dataSource = DataSource.LOCAL)
            d("Song " + songs.toString())
        }

    }
}