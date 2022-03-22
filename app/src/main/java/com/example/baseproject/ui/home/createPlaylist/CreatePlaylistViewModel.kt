package com.example.baseproject.ui.home.createPlaylist

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.core.base.BaseViewModel
import com.example.mediaservice.MediaServiceConnection
import com.example.mediaservice.repository.PlaylistRepository.PlaylistRepository
import com.example.mediaservice.repository.models.MediaIdExtra
import com.example.mediaservice.repository.models.Playlist
import com.example.mediaservice.utils.CMD_CREATE_PLAYLIST
import com.example.mediaservice.utils.DataSource
import com.example.mediaservice.utils.DataSource.LOCAL
import com.example.mediaservice.utils.KEY_PLAYLIST
import com.example.mediaservice.utils.MediaType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePlaylistViewModel @Inject constructor(private val mediaServiceConnection: MediaServiceConnection): BaseViewModel() {
    private var _createPlaylistCompleted = MutableLiveData<Boolean>(false)
    var createPlaylistCompleted : LiveData<Boolean> = _createPlaylistCompleted

    var playlistTitle : String = ""
    var mediaIdExtra : MediaIdExtra? = null

    fun createPlaylist(title: String){
        viewModelScope.launch {
            isLoading.postValue(true)
            _createPlaylistCompleted.postValue(false)
            mediaServiceConnection.sendCommand(CMD_CREATE_PLAYLIST, Bundle().apply { putString(KEY_PLAYLIST,title) }, resultCallback = { resultCode, bundle ->
                if(resultCode == 200) {
                    val playlistId = bundle?.getLong("playListId") ?: -1
                    playlistTitle = title
                    mediaIdExtra = MediaIdExtra(mediaType = MediaType.TYPE_PLAYLIST,id = playlistId, dataSource = DataSource.NONE)
                    isLoading.postValue(false)
                    _createPlaylistCompleted.postValue(true)
                }
            })
        }
    }
}