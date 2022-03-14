package com.example.baseproject.ui.tabLocalMusic

import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.support.v4.media.MediaBrowserCompat
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.baseproject.model.MediaItemUI
import com.example.core.base.BaseViewModel
import com.example.mediaservice.MediaServiceConnection
import com.example.mediaservice.repository.models.MediaIdExtra
import com.example.mediaservice.utils.DataSource
import com.example.mediaservice.utils.MediaType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocalMusicViewModel @Inject constructor(private val mediaServiceConnection: MediaServiceConnection) : BaseViewModel() {

    private val rootMediaId = mediaServiceConnection.rootMediaId

    private val _mediaItems = MutableLiveData<List<MediaItemUI>>(emptyList())
    val mediaItems : LiveData<List<MediaItemUI>> = _mediaItems

    private val subscriptionCallback = object: MediaBrowserCompat.SubscriptionCallback(){
        override fun onChildrenLoaded(parentId: String, children: MutableList<MediaBrowserCompat.MediaItem>) {
            viewModelScope.launch(Dispatchers.Default) {
                val listMediaItemExtra = children.map {
                    val mediaIdExtra = MediaIdExtra.getDataFromString(it.mediaId ?: "")
                    val id: Long= mediaIdExtra.id ?: -1
                    val title: String = it.description.title.toString()
                    val subTitle: String = it.description.subtitle.toString()
                    val iconUri: Uri = it.description.iconUri ?: Uri.EMPTY
                    val isBrowsable: Boolean = it.flags.equals(MediaBrowserCompat.MediaItem.FLAG_BROWSABLE)
                    val mediaType = mediaIdExtra.mediaType ?: -1
                    val dataSource = mediaIdExtra.dataSource
                    MediaItemUI(mediaIdExtra = it.mediaId ?: "",id = id, title = title, subTitle = subTitle , iconUri = iconUri, isBrowsable = isBrowsable, dataSource = dataSource, mediaType = mediaType)
                }
                Log.d("TAG", "onChildrenLoaded: " + listMediaItemExtra.toString())
                _mediaItems.postValue(listMediaItemExtra)
            }
        }
    }

    fun startLoadingData() {
        mediaServiceConnection.subscribe(rootMediaId,subscriptionCallback)
    }

    override fun onCleared() {
        super.onCleared()
        mediaServiceConnection.unsubscribe(rootMediaId,subscriptionCallback)
    }
}
