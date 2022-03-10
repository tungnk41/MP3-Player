package com.example.baseproject.ui.login

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.example.baseproject.model.MediaItemExtra
import com.example.baseproject.ui.tabLocalMusic.LocalMusicViewModel
import com.example.core.base.BaseViewModel
import com.example.mediaservice.MediaServiceConnection
import com.example.mediaservice.utils.*
import com.example.mediaservice.extensions.currentPlayBackPosition
import com.example.mediaservice.repository.models.MediaIdExtra
import com.example.mediaservice.repository.models.Playlist
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val mediaServiceConnection: MediaServiceConnection) : BaseViewModel(){

    companion object{
        const val TAG = "LoginViewModel"
    }

    private val mediaIdExtra = MediaIdExtra(MediaType.TYPE_ALL_SONGS,null, DataSource.LOCAL)
    private val handler = Handler(Looper.getMainLooper())

    private val _mediaItems = MutableLiveData<List<MediaItemExtra>>(emptyList())
    val mediaItems : LiveData<List<MediaItemExtra>> = _mediaItems

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

    private val subscriptionCallback = object: MediaBrowserCompat.SubscriptionCallback(){
        override fun onChildrenLoaded(parentId: String, children: MutableList<MediaBrowserCompat.MediaItem>) {
            Log.d("TAG", "onChildrenLoaded: " + children.toString())
            viewModelScope.launch(Dispatchers.Default) {
                val listMediaItemExtra = children.map {
                        val mediaIdExtra = MediaIdExtra.getDataFromString(it.mediaId ?: "")
                        val id: Long= mediaIdExtra.id ?: -1
                        val title: String = it.description.title.toString()
                        val iconUri: Uri = it.description.iconUri ?: Uri.EMPTY
                        val isBrowsable: Boolean = it.flags.equals(MediaBrowserCompat.MediaItem.FLAG_BROWSABLE)
                        val mediaType = mediaIdExtra.mediaType ?: -1
                        val dataSource = mediaIdExtra.dataSource
                        MediaItemExtra(mediaIdExtra = it.mediaId ?: "",id = id, title = title, iconUri = iconUri, isBrowsable = isBrowsable, dataSource = dataSource, mediaType = mediaType)
                    }
                    _mediaItems.postValue(listMediaItemExtra)
                }
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

    fun repeate() {
        mediaServiceConnection.transportControls.setRepeatMode(PlaybackStateCompat.REPEAT_MODE_ONE)
    }

    fun shuffle() {
        mediaServiceConnection.transportControls.setShuffleMode(PlaybackStateCompat.SHUFFLE_MODE_ALL)
    }
    fun sendPlaylist() {
        val playlist = Playlist(title = "title playlist", iconUri = "", userId = -1)
        val bundle : Bundle = Bundle().apply { putParcelable(KEY_PLAYLIST,playlist) }
        mediaServiceConnection.sendCommand(CMD_ADD_PLAYLIST,bundle)
    }

    fun getPlaylist() {
        val mediaIdExtra = MediaIdExtra(MediaType.TYPE_ALL_PLAYLISTS,null, DataSource.LOCAL)
        mediaServiceConnection.subscribe(mediaIdExtra.toString(),subscriptionCallback)
    }

    fun seekTo() {
        val position = _playbackState.value?.currentPlayBackPosition
        position?.let {
            mediaServiceConnection.transportControls.seekTo(it + 5000)
        }
    }

    //View
    fun updatePosition(): Boolean = handler.postDelayed({
        val position = _playbackState.value?.currentPlayBackPosition?.div(1000) ?: 0
        if(position != currentPosition){
            currentPosition = position
            Log.d(LocalMusicViewModel.TAG, "updatePosition: " + currentPosition)
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