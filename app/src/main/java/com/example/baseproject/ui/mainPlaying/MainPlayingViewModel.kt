package com.example.baseproject.ui.mainPlaying

import com.example.core.base.BaseViewModel
import com.example.mediaservice.MediaServiceConnection
import com.example.mediaservice.utils.CMD_CLICK_FAVORITE
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainPlayingViewModel @Inject constructor(private val mediaServiceConnection: MediaServiceConnection) : BaseViewModel() {

    fun sendCmdUpdateFavorite() {
        mediaServiceConnection.sendCommand(CMD_CLICK_FAVORITE,null)
    }
}