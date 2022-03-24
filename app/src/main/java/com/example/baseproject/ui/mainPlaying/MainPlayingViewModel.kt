package com.example.baseproject.ui.mainPlaying

import android.os.Bundle
import com.example.core.base.BaseViewModel
import com.example.mediaservice.MediaServiceConnection
import com.example.mediaservice.utils.CMD_CLICK_FAVORITE
import com.example.mediaservice.utils.KEY_MEDIA_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainPlayingViewModel @Inject constructor(private val mediaServiceConnection: MediaServiceConnection) : BaseViewModel() {

    fun sendCmdUpdateFavorite(mediaIdExtra: String) {
        mediaServiceConnection.sendCommand(CMD_CLICK_FAVORITE, Bundle().apply { putString(KEY_MEDIA_ID,mediaIdExtra) })
    }
}