package com.example.baseproject.ui.home.createPlaylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.core.base.BaseViewModel
import com.example.mediaservice.repository.PlaylistRepository.PlaylistRepository
import com.example.mediaservice.repository.models.Playlist
import com.example.mediaservice.utils.DataSource.LOCAL
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePlaylistViewModel @Inject constructor(private val playlistRepository: PlaylistRepository): BaseViewModel() {
    private var _createPlaylistCompleted = MutableLiveData<Boolean>(false)
    var createPlaylistCompleted : LiveData<Boolean> = _createPlaylistCompleted

    fun createPlaylist(title: String){
        val playlist = Playlist(title = title, userId = 1)
        viewModelScope.launch {
            isLoading.postValue(true)
            _createPlaylistCompleted.postValue(false)
            playlistRepository.insert(playlist)
            isLoading.postValue(false)
            _createPlaylistCompleted.postValue(true)
        }
    }
}