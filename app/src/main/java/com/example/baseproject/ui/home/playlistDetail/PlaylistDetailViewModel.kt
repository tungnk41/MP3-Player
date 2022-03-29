package com.example.baseproject.ui.home.playlistDetail

import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.baseproject.model.MediaItemUI
import com.example.core.base.BaseViewModel
import com.example.mediaservice.MediaServiceConnection
import com.example.mediaservice.repository.PlaylistRepository.PlaylistRepository
import com.example.mediaservice.repository.SongRepository.SongRepository
import com.example.mediaservice.repository.models.MediaIdExtra
import com.example.mediaservice.utils.DataSource
import com.example.mediaservice.utils.MediaType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber.d
import javax.inject.Inject

@HiltViewModel
class PlaylistDetailViewModel @Inject constructor(private val mediaServiceConnection: MediaServiceConnection): BaseViewModel() {

    var mediaIdExtra: MediaIdExtra? = null

    private val _listSongPlaylist = MutableLiveData<List<MediaItemUI>>(emptyList())
    val listSongPlaylist : LiveData<List<MediaItemUI>> = _listSongPlaylist

    private val subscriptionCallback = object: MediaBrowserCompat.SubscriptionCallback(){
        override fun onChildrenLoaded(parentId: String, children: MutableList<MediaBrowserCompat.MediaItem>) {
            viewModelScope.launch(Dispatchers.Default) {
                val listSong = children.map{
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
                _listSongPlaylist.postValue(listSong)
                isLoading.postValue(false)
            }
        }
        override fun onError(parentId: String) {
            super.onError(parentId)
            isLoading.postValue(false)
        }
    }

    fun connectToMediaService() {
        isLoading.value = true
        mediaServiceConnection.subscribe(mediaIdExtra.toString(),subscriptionCallback)
    }

    fun disconnectToMediaService() {
        mediaServiceConnection.unsubscribe(mediaIdExtra.toString(),subscriptionCallback)
    }

    fun playAtIndex(index: Int){
        val bundle = Bundle()
        bundle.putInt("index", index)
        mediaServiceConnection.transportControls.playFromMediaId(_listSongPlaylist?.value?.get(index)?.mediaIdExtra.toString(), bundle)
    }
}