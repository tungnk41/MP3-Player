package com.example.baseproject.ui.home.song

import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.baseproject.model.MediaItemUI
import com.example.core.base.BaseViewModel
import com.example.mediaservice.MediaServiceConnection
import com.example.mediaservice.repository.models.MediaIdExtra
import com.example.mediaservice.utils.DataSource
import com.example.mediaservice.utils.MediaType.TYPE_ALL_SONGS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongViewModel @Inject constructor(private val mediaServiceConnection: MediaServiceConnection) : BaseViewModel() {
    private var mediaIdExtra = MediaIdExtra()
    private var listSongMediaItemUI = listOf<MediaItemUI>()

    private val _mediaItems = MutableLiveData<List<MediaItemUI>>(emptyList())
    val mediaItems : LiveData<List<MediaItemUI>> = _mediaItems

    private val subscriptionCallback = object: MediaBrowserCompat.SubscriptionCallback(){
        override fun onChildrenLoaded(parentId: String, children: MutableList<MediaBrowserCompat.MediaItem>) {
            viewModelScope.launch(Dispatchers.Default) {
                 listSongMediaItemUI = children.map{
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
                _mediaItems.postValue(listSongMediaItemUI)
            }
        }
    }

    fun startLoadingData(parentMediaIdExtra : MediaIdExtra) {
        mediaIdExtra = parentMediaIdExtra
        mediaServiceConnection.subscribe(mediaIdExtra.toString(),subscriptionCallback)
    }

    override fun onCleared() {
        super.onCleared()
        mediaServiceConnection.unsubscribe(mediaIdExtra.toString(),subscriptionCallback)
    }

    fun playAtIndex(index: Int){
        val bundle = Bundle()
        bundle.putInt("index", index)
        mediaServiceConnection.transportControls.playFromMediaId(listSongMediaItemUI[index].mediaIdExtra.toString(), bundle)
    }
}