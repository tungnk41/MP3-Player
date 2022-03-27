package com.example.mediaservice

import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mediaservice.extensions.EMPTY_MEDIA_METADATA_COMPAT
import com.example.mediaservice.extensions.EMPTY_PLAYBACK_STATE
import com.example.mediaservice.utils.NETWORK_FAILURE
import timber.log.Timber
import timber.log.Timber.d
import java.util.*
import javax.inject.Inject

class MediaServiceConnection @Inject constructor(val context: Context, serviceComponent: ComponentName) {

    companion object {
        const val TAG = "MediaServiceConnection"
    }
    private lateinit var mediaController: MediaControllerCompat
    val transportControls : MediaControllerCompat.TransportControls
        get() = mediaController.transportControls

    //Expose to Viewmodel
    val rootMediaId: String get() = mediaBrowser.root

    private var mPlaybackState = MutableLiveData<PlaybackStateCompat>().apply { postValue(EMPTY_PLAYBACK_STATE) }
    val playbackState: LiveData<PlaybackStateCompat> = mPlaybackState

    private var mMediaMetadataCompat = MutableLiveData<MediaMetadataCompat>().apply { postValue(EMPTY_MEDIA_METADATA_COMPAT) }
    val mediaMetadataCompat: LiveData<MediaMetadataCompat> = mMediaMetadataCompat

    private var mIsConnected = MutableLiveData<Boolean>().apply { postValue(false) }
    val isConnected: LiveData<Boolean> = mIsConnected

    private val mNetworkFailure = MutableLiveData<Boolean>().apply { postValue(false) }
    val networkFailure: LiveData<Boolean> = mNetworkFailure

    private var mRepeatMode = MutableLiveData<Int>().apply { postValue(PlaybackStateCompat.REPEAT_MODE_NONE) }
    val repeatMode: LiveData<Int> = mRepeatMode

    private var mShuffleMode = MutableLiveData<Int>().apply { postValue(PlaybackStateCompat.SHUFFLE_MODE_NONE) }
    val shuffleMode: LiveData<Int> = mShuffleMode

    private val mediaBrowserConnectionCallback = MediaBrowserConnectionCallback(context)
    private val mediaBrowser = MediaBrowserCompat(context, serviceComponent, mediaBrowserConnectionCallback, null)
        .apply {
            connect()
        }

    fun subscribe(parentId: String, callback: MediaBrowserCompat.SubscriptionCallback) {
        mediaBrowser.subscribe(parentId, callback)
    }

    fun unsubscribe(parentId: String, callback: MediaBrowserCompat.SubscriptionCallback) {
        mediaBrowser.unsubscribe(parentId, callback)
    }

    fun registerCallback(callback: MediaControllerCompat.Callback) {
        mediaController.registerCallback(callback)
    }

    fun unregisterCallback(callback: MediaControllerCompat.Callback) {
        mediaController.unregisterCallback(callback)
    }

    fun sendCommand(command: String, parameters: Bundle?) =
        sendCommand(command, parameters) { _, _ -> }

    fun sendCommand(
        command: String,
        parameters: Bundle?,
        resultCallback: ((Int, Bundle?) -> Unit)?
    ) {
        if (mediaBrowser.isConnected) {
            mediaController.sendCommand(command, parameters, object : ResultReceiver(Handler()) {
                override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
                    resultCallback?.let {
                        resultCallback(resultCode, resultData)
                    }
                }
            })
        }
    }

    private inner class MediaBrowserConnectionCallback(private val context: Context) : MediaBrowserCompat.ConnectionCallback() {
        override fun onConnected() {
            Log.d(TAG, "onConnected: ")
            mediaController = MediaControllerCompat(context, mediaBrowser.sessionToken)
            mediaController.registerCallback(MediaControllerCallback())
            mIsConnected.postValue(true)
        }

        override fun onConnectionFailed() {
            mIsConnected.postValue(false)
        }

        override fun onConnectionSuspended() {
            mIsConnected.postValue(false)
        }
    }

    private inner class MediaControllerCallback : MediaControllerCompat.Callback() {
        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            mPlaybackState.postValue(state ?: EMPTY_PLAYBACK_STATE)
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            mMediaMetadataCompat.postValue(metadata ?: EMPTY_MEDIA_METADATA_COMPAT)
        }

        override fun onRepeatModeChanged(repeatMode: Int) {
            mRepeatMode.postValue(repeatMode)
        }

        override fun onShuffleModeChanged(shuffleMode: Int) {
            mShuffleMode.postValue(shuffleMode)
        }

        override fun onSessionEvent(event: String?, extras: Bundle?) {
            super.onSessionEvent(event, extras)
            when (event) {
                NETWORK_FAILURE -> mNetworkFailure.postValue(true)
            }
        }

        override fun onSessionDestroyed() {
            super.onSessionDestroyed()
        }
    }
}
