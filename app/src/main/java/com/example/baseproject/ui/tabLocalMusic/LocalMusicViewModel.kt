package com.example.baseproject.ui.tabLocalMusic

import com.example.core.base.BaseViewModel
import com.example.mediaservice.MediaServiceConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LocalMusicViewModel @Inject constructor(private val mediaServiceConnection: MediaServiceConnection) : BaseViewModel() {

    companion object{
        const val TAG = "LocalMusicViewModel"
    }


}
