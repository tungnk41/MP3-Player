package com.example.baseproject.ui.home.artistDetail

import android.net.Uri
import android.support.v4.media.MediaBrowserCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.baseproject.model.MediaItemUI
import com.example.core.base.BaseViewModel
import com.example.mediaservice.MediaServiceConnection
import com.example.mediaservice.repository.models.MediaIdExtra
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ArtistDetailViewModel @Inject constructor(private val mediaServiceConnection: MediaServiceConnection) : BaseViewModel() {

    private var currentMediaIdExtra: MediaIdExtra? = null

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
                    MediaItemUI(mediaIdExtra = mediaIdExtra,id = id, title = title, subTitle = subTitle , iconUri = iconUri, isBrowsable = isBrowsable, dataSource = dataSource, mediaType = mediaType)
                }
                Timber.d("onChildrenLoaded " + listMediaItemExtra.toString())
                _mediaItems.postValue(listMediaItemExtra)
                isLoading.postValue(false)
            }
        }
    }

    fun startLoadingData(mediaIdExtra: MediaIdExtra) {
        if(currentMediaIdExtra.hashCode() != mediaIdExtra.hashCode()) {
            currentMediaIdExtra = mediaIdExtra
            isLoading.value = true
            mediaServiceConnection.subscribe(currentMediaIdExtra.toString(),subscriptionCallback)
        }
    }

    override fun onCleared() {
        super.onCleared()
        mediaServiceConnection.unsubscribe(currentMediaIdExtra.toString(),subscriptionCallback)
    }
}