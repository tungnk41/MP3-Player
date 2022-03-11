package com.example.baseproject.ui.bottomController

import android.os.Handler
import android.os.Looper
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.baseproject.ui.tabLocalMusic.LocalMusicViewModel
import com.example.baseproject.utils.PROGRESS_BAR_MAX_VALUE
import com.example.core.base.BaseViewModel
import com.example.mediaservice.MediaServiceConnection
import com.example.mediaservice.extensions.currentPlayBackPosition
import com.example.mediaservice.extensions.duration
import com.example.mediaservice.extensions.isPlaying
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber.d
import javax.inject.Inject

@HiltViewModel
class BottomControllerViewModel @Inject constructor() : BaseViewModel() {

}