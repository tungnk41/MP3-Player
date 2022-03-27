package com.example.baseproject.ui.home.createPlaylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.core.base.BaseViewModel
import com.example.mediaservice.repository.PlaylistRepository.PlaylistRepository
import com.example.mediaservice.repository.models.MediaIdExtra
import com.example.mediaservice.repository.models.Playlist
import com.example.mediaservice.session.UserSessionInfo
import com.example.mediaservice.utils.DataSource
import com.example.mediaservice.utils.MediaType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePlaylistViewModel @Inject constructor(private val playlistRepository: PlaylistRepository, private val userSessionInfo: UserSessionInfo): BaseViewModel() {
    private var _createPlaylistCompleted = MutableLiveData<Boolean>(false)
    var createPlaylistCompleted : LiveData<Boolean> = _createPlaylistCompleted

    var playlistTitle : String = ""
    var mediaIdExtra : MediaIdExtra? = null

    private var _title: String? = null

    fun createPlaylist(title: String){
        _title = title
        viewModelScope.launch {
            isLoading.postValue(true)
            _createPlaylistCompleted.postValue(false)
            val playlistId = playlistRepository.insert(Playlist(title = title, iconUri = "", userId = userSessionInfo.userId))
            playlistTitle = _title ?: ""
            mediaIdExtra = MediaIdExtra(
                mediaType = MediaType.TYPE_PLAYLIST,
                id = playlistId,
                dataSource = DataSource.NONE
            )
            isLoading.postValue(false)
            _createPlaylistCompleted.postValue(true)
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}