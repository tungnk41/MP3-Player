package com.example.baseproject.container

import android.os.Handler
import android.os.Looper
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v4.media.session.PlaybackStateCompat.*
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.example.baseproject.utils.PROGRESS_BAR_MAX_VALUE
import com.example.baseproject.utils.convertToTimeString
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

    private var mIsUpdatingProgress : Boolean= false
    private var mDuration : Long= 0

    private val mCurrentProgress = MutableLiveData<Int>(0)
    val currentProgress: LiveData<Int> = mCurrentProgress

    private val mDurationUI = MutableLiveData<String>("00:00")
    val durationUI: LiveData<String> = mDurationUI

    private val mCurrentPositionUI = MutableLiveData<String>("00:00")
    val currentPositionUI: LiveData<String> = mCurrentPositionUI

    private val mMediaMetadata = MutableLiveData<MediaMetadataCompat>()
    val mediaMetadata: LiveData<MediaMetadataCompat> = mMediaMetadata

    private val mPlaybackState = MutableLiveData<PlaybackStateCompat>()
    val playbackState: LiveData<PlaybackStateCompat> = mPlaybackState

    private val mIsPlaying = MutableLiveData<Boolean>(false)
    val isPlaying: LiveData<Boolean> = mIsPlaying

    private val mRepeatMode = MutableLiveData<Int>(PlaybackStateCompat.REPEAT_MODE_NONE)
    val repeatMode: LiveData<Int> = mRepeatMode

    private val mShuffleMode = MutableLiveData<Int>(PlaybackStateCompat.SHUFFLE_MODE_NONE)
    val shuffleMode: LiveData<Int> = mShuffleMode

    private val playbackStateObserver = Observer<PlaybackStateCompat> {
        mPlaybackState.value = it
        mIsPlaying.value = it.isPlaying
    }
    private val mediaMetadataObserver = Observer<MediaMetadataCompat> {
        mMediaMetadata.value = it
        mDuration = it.duration
        mDurationUI.value = convertToTimeString(mDuration)
    }
    private val repeatModeObserver = Observer<Int> {
        mRepeatMode.value = it
    }
    private val shuffleModeObserver = Observer<Int> {
        mShuffleMode.value = it
    }


    init {
        mediaServiceConnection.playbackState.observeForever(playbackStateObserver)
        mediaServiceConnection.mediaMetadataCompat.observeForever(mediaMetadataObserver)
        mediaServiceConnection.repeatMode.observeForever(repeatModeObserver)
        mediaServiceConnection.shuffleMode.observeForever(shuffleModeObserver)
    }


    fun prev() {
        mediaServiceConnection.transportControls.skipToPrevious()
    }

    fun playPauseToggle() {
        if(mIsPlaying.value == true){
            pause()
        }else {
            play()
        }
    }

    fun play() {
        mIsPlaying.value = true
        mediaServiceConnection.transportControls.play()
    }

    fun pause() {
        mIsPlaying.value = false
        mediaServiceConnection.transportControls.pause()
    }

    fun next() {
        mediaServiceConnection.transportControls.skipToNext()
    }

    fun seekTo(pos : Int) {
        viewModelScope.launch {
            val playbackPosition: Long = withContext(Dispatchers.Default) {
                ((pos.toDouble()/PROGRESS_BAR_MAX_VALUE)*mDuration).toLong()
            }
            mediaServiceConnection.transportControls.seekTo(playbackPosition)
        }
    }

    fun nextRepeatMode() {
        val nextRepeatMode = when(repeatMode.value){
            REPEAT_MODE_NONE -> REPEAT_MODE_ALL
            REPEAT_MODE_ALL -> REPEAT_MODE_ONE
            REPEAT_MODE_ONE -> REPEAT_MODE_NONE
            else -> REPEAT_MODE_NONE
        }
        mediaServiceConnection.transportControls.setRepeatMode(nextRepeatMode)
    }

    fun nextShuffleMode() {
        val nextShuffleMode = when(shuffleMode.value){
            SHUFFLE_MODE_NONE -> SHUFFLE_MODE_ALL
            SHUFFLE_MODE_ALL -> SHUFFLE_MODE_NONE
            else -> SHUFFLE_MODE_NONE
        }
        mediaServiceConnection.transportControls.setShuffleMode(nextShuffleMode)
    }


    fun startUpdateProgress() {
        mIsUpdatingProgress = true
        viewModelScope.launch {
            while (mIsUpdatingProgress && isPlaying.hasObservers()) {
                d("isUpdatingProgress")
                val currentPosition = mPlaybackState.value?.currentPlayBackPosition ?: 0
                val currentProgress : Double = withContext(Dispatchers.Default){
                    ((currentPosition.toDouble())/mDuration)* PROGRESS_BAR_MAX_VALUE
                }
                mCurrentProgress.value = currentProgress?.toInt()
                mCurrentPositionUI.value = convertToTimeString(currentPosition)
                delay(1000)
            }
        }
    }

    fun stopUpdateProgress() {
        mIsUpdatingProgress = false
    }

    override fun onCleared() {
        super.onCleared()
        stopUpdateProgress()
        mediaServiceConnection.playbackState.removeObserver(playbackStateObserver)
        mediaServiceConnection.mediaMetadataCompat.removeObserver(mediaMetadataObserver)
        mediaServiceConnection.repeatMode.removeObserver(repeatModeObserver)
        mediaServiceConnection.shuffleMode.removeObserver(shuffleModeObserver)
    }

}