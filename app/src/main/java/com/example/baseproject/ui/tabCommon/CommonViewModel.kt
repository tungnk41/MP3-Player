package com.example.baseproject.ui.tabCommon

import android.os.Handler
import android.os.Looper
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.core.base.BaseViewModel
import com.example.mediaservice.MediaServiceConnection
import com.example.mediaservice.const.LOCAL_DATA
import com.example.mediaservice.const.REMOTE_DATA
import com.example.mediaservice.const.TYPE_ALL_SONGS
import com.example.mediaservice.extensions.currentPlayBackPosition
import com.example.mediaservice.repository.models.MediaIdExtra
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CommonViewModel @Inject constructor(private val mediaServiceConnection: MediaServiceConnection) : BaseViewModel() {

    companion object{
        const val TAG = "CommonViewModel"
    }

    private val mediaIdExtra = MediaIdExtra(TYPE_ALL_SONGS,null, LOCAL_DATA)
    private val handler = Handler(Looper.getMainLooper())

    private val _mediaItems = MutableLiveData<List<MediaBrowserCompat.MediaItem>>(emptyList())
    val mediaItems : LiveData<List<MediaBrowserCompat.MediaItem>> = _mediaItems

    private val _mediaPosition = MutableLiveData<Long>(0)
    val mediaPosition: LiveData<Long> = _mediaPosition

    private val _mediaMetadata = MutableLiveData<MediaMetadataCompat>()
    val mediaMetadata: LiveData<MediaMetadataCompat> = _mediaMetadata

    private val _playbackState = MutableLiveData<PlaybackStateCompat>()
    val playbackState: LiveData<PlaybackStateCompat> = _playbackState

    private var currentPosition: Long = 0

    private val playbackStateObserver = Observer<PlaybackStateCompat> {
        _playbackState.value = it
        updatePosition()
    }

    private val mediaMetadataObserver = Observer<MediaMetadataCompat> {
        _mediaMetadata.value = it
    }

    init {
        mediaServiceConnection.playbackState.observeForever(playbackStateObserver)
        mediaServiceConnection.mediaMetadataCompat.observeForever(mediaMetadataObserver)
    }

    private val subscriptionCallback = object:MediaBrowserCompat.SubscriptionCallback(){
        override fun onChildrenLoaded(parentId: String, children: MutableList<MediaBrowserCompat.MediaItem>) {
            Log.d("TAG", "onChildrenLoaded: " + children.toString())
            _mediaItems.value = children
        }
    }

    fun connect() {
        mediaServiceConnection.subscribe(mediaIdExtra.toString(),subscriptionCallback)
    }

    fun prev() {
        mediaServiceConnection.transportControls.skipToPrevious()
    }

    fun play() {
        mediaServiceConnection.transportControls.playFromMediaId("1",null)
    }

    fun next() {
        mediaServiceConnection.transportControls.skipToNext()
    }

    fun search() {
        mediaServiceConnection.transportControls.prepareFromSearch("abc",null)
    }

    fun sendCommand() {
        mediaServiceConnection.sendCommand("command 1", null)
    }

    fun sendPlaylist() {

    }

    fun getPlaylist() {

    }

    //View
    fun updatePosition(): Boolean = handler.postDelayed({
        val position = _playbackState.value?.currentPlayBackPosition?.div(1000) ?: 0
        if(position != currentPosition){
            currentPosition = position
            Log.d(TAG, "updatePosition: " + currentPosition)
//        updatePosition()
        }
    },1000)

    override fun onCleared() {
        super.onCleared()
        mediaServiceConnection.unsubscribe(mediaIdExtra.toString(),subscriptionCallback)
        mediaServiceConnection.playbackState.removeObserver(playbackStateObserver)
        mediaServiceConnection.mediaMetadataCompat.removeObserver(mediaMetadataObserver)
    }
}
