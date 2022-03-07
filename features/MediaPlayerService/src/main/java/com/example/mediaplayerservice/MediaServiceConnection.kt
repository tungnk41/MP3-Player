package com.example.mediaplayerservice

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
import androidx.lifecycle.MutableLiveData
import com.example.mediaplayerservice.const.NETWORK_FAILURE
import javax.inject.Inject

class MediaServiceConnection @Inject constructor(val context: Context, val serviceComponent: ComponentName) {

    companion object {
        const val TAG = "MediaServiceConnection"
    }
    private lateinit var mediaController: MediaControllerCompat
    val transportControls : MediaControllerCompat.TransportControls
        get() = mediaController.transportControls


    //Expose to Viewmodel
    val rootMediaId: String get() = mediaBrowser.root

    val playbackState = MutableLiveData<PlaybackStateCompat>()
        .apply { postValue(EMPTY_PLAYBACK_STATE) }
    val nowPlaying = MutableLiveData<MediaMetadataCompat>()
        .apply { postValue(NOTHING_PLAYING) }

    val isConnected = MutableLiveData<Boolean>()
        .apply { postValue(false) }
    val networkFailure = MutableLiveData<Boolean>()
        .apply { postValue(false) }


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
        resultCallback: ((Int, Bundle?) -> Unit)
    ) {
        if (mediaBrowser.isConnected) {
            mediaController.sendCommand(command, parameters, object : ResultReceiver(Handler()) {
                override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
                    resultCallback(resultCode, resultData)
                }
            })
        }
    }

    private inner class MediaBrowserConnectionCallback(private val context: Context) : MediaBrowserCompat.ConnectionCallback() {
        override fun onConnected() {
            mediaController = MediaControllerCompat(context, mediaBrowser.sessionToken).apply {
                Log.d(TAG, "onConnected: ")
                registerCallback(MediaControllerCallback())
            }
            isConnected.postValue(true)

        }

        override fun onConnectionFailed() {
            isConnected.postValue(false)
        }

        override fun onConnectionSuspended() {
            isConnected.postValue(false)
        }
    }

    private inner class MediaControllerCallback : MediaControllerCompat.Callback() {
        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            playbackState.postValue(state ?: EMPTY_PLAYBACK_STATE)
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            nowPlaying.postValue(metadata)
        }

        override fun onSessionEvent(event: String?, extras: Bundle?) {
            super.onSessionEvent(event, extras)
            when (event) {
                NETWORK_FAILURE -> networkFailure.postValue(true)
            }
        }

        override fun onSessionDestroyed() {
            super.onSessionDestroyed()
        }
    }

}


@Suppress("PropertyName")
val EMPTY_PLAYBACK_STATE: PlaybackStateCompat = PlaybackStateCompat.Builder()
    .setState(PlaybackStateCompat.STATE_NONE, 0, 0f)
    .build()

@Suppress("PropertyName")
val NOTHING_PLAYING: MediaMetadataCompat = MediaMetadataCompat.Builder()
    .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, "")
    .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, 0)
    .build()