package com.example.baseproject.container

import android.os.Handler
import android.os.Looper
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.example.baseproject.utils.PROGRESS_BAR_MAX_VALUE
import com.example.core.base.BaseViewModel
import com.example.mediaservice.MediaServiceConnection
import com.example.mediaservice.extensions.currentPlayBackPosition
import com.example.mediaservice.extensions.duration
import com.example.mediaservice.extensions.isPlaying
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import timber.log.Timber.d
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val mediaServiceConnection: MediaServiceConnection) : BaseViewModel() {

    private var duration : Long= 0
    private var isUpdatingProgress : Boolean= false

    private val _currentProgress = MutableLiveData<Int>(0)
    val currentProgress: LiveData<Int> = _currentProgress

    private val _mediaMetadata = MutableLiveData<MediaMetadataCompat>()
    val mediaMetadata: LiveData<MediaMetadataCompat> = _mediaMetadata

    private val _playbackState = MutableLiveData<PlaybackStateCompat>()
    val playbackState: LiveData<PlaybackStateCompat> = _playbackState

    private val _isPlaying = MutableLiveData<Boolean>(false)
    val isPlaying: LiveData<Boolean> = _isPlaying

    private val playbackStateObserver = Observer<PlaybackStateCompat> {
        _playbackState.value = it
        _isPlaying.value = it.isPlaying
    }
    private val mediaMetadataObserver = Observer<MediaMetadataCompat> {
        _mediaMetadata.value = it
        duration = it.duration
    }


    init {
        mediaServiceConnection.playbackState.observeForever(playbackStateObserver)
        mediaServiceConnection.mediaMetadataCompat.observeForever(mediaMetadataObserver)
    }


    fun prev() {
        mediaServiceConnection.transportControls.skipToPrevious()
    }

    fun playPauseToggle() {
        _isPlaying.value?.let {
            _isPlaying.value = !_isPlaying.value!!
        }
        if(_isPlaying.value == true){
            mediaServiceConnection.transportControls.pause()
        }else {
            mediaServiceConnection.transportControls.play()
        }
    }

    fun play() {
        _isPlaying.value = true
        mediaServiceConnection.transportControls.play()
    }

    fun pause() {
        _isPlaying.value = false
        mediaServiceConnection.transportControls.pause()
    }

    fun next() {
        mediaServiceConnection.transportControls.skipToNext()
    }

    fun seekTo(pos : Int) {
        viewModelScope.launch {
            val playbackPosition: Long = withContext(Dispatchers.Default) {
                ((pos.toDouble()/PROGRESS_BAR_MAX_VALUE)*duration).toLong()
            }
            mediaServiceConnection.transportControls.seekTo(playbackPosition)
        }
    }


//    private fun updateProgress(): Boolean = handler.postDelayed({
//        if(duration != 0L){
//            val currentProgress : Double = (((_playbackState.value?.currentPlayBackPosition ?: 0).toDouble())/duration)* PROGRESS_BAR_MAX_VALUE
//            _currentProgress.value = currentProgress?.toInt()
//            updateProgress()
//        }
//    },1000)

    fun startUpdateProgress() {
        isUpdatingProgress = true
        viewModelScope.launch {
            while (isUpdatingProgress && isPlaying.hasObservers()) {
                d("Updating Progress")
                val currentProgress : Double = withContext(Dispatchers.Default){
                    (((_playbackState.value?.currentPlayBackPosition ?: 0).toDouble())/duration)* PROGRESS_BAR_MAX_VALUE
                }
                _currentProgress.value = currentProgress?.toInt()
                delay(1000)
            }
        }
    }

    fun stopUpdateProgress() {
        isUpdatingProgress = false
    }

    override fun onCleared() {
        super.onCleared()
        stopUpdateProgress()
        mediaServiceConnection.playbackState.removeObserver(playbackStateObserver)
        mediaServiceConnection.mediaMetadataCompat.removeObserver(mediaMetadataObserver)
    }

}