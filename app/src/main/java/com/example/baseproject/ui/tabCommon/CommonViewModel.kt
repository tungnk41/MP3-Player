package com.example.baseproject.ui.tabCommon

import android.support.v4.media.MediaBrowserCompat
import android.util.Log
import com.example.core.base.BaseViewModel
import com.example.mediaservice.MediaServiceConnection
import com.example.mediaservice.const.LOCAL_DATA
import com.example.mediaservice.const.TYPE_ALL_SONGS
import com.example.mediaservice.repository.models.MediaIdExtra
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CommonViewModel @Inject constructor(val mediaServiceConnection: MediaServiceConnection) : BaseViewModel() {

    val mediaIdExtra = MediaIdExtra(TYPE_ALL_SONGS,null, LOCAL_DATA)

    fun connect() {
        mediaServiceConnection.subscribe(mediaIdExtra.toString(),object : MediaBrowserCompat.SubscriptionCallback(){
            override fun onChildrenLoaded(
                parentId: String,
                children: MutableList<MediaBrowserCompat.MediaItem>
            ) {
                Log.d("TAG", "onChildrenLoaded: " + children.toString())
            }
        })
    }

    fun prev() {

    }

    fun play() {
        mediaServiceConnection.transportControls.playFromMediaId("1",null)
    }

    fun next() {

    }
}
