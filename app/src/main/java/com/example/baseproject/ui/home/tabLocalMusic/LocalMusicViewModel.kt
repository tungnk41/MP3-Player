package com.example.baseproject.ui.home.tabLocalMusic

import android.net.Uri
import android.os.Parcelable
import android.support.v4.media.MediaBrowserCompat
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.baseproject.model.MediaItemUI
import com.example.core.base.BaseViewModel
import com.example.mediaservice.MediaServiceConnection
import com.example.mediaservice.extensions.toBrowserMediaItem
import com.example.mediaservice.repository.PlaylistRepository.PlaylistRepository
import com.example.mediaservice.repository.models.MediaIdExtra
import com.example.mediaservice.repository.models.Playlist
import com.example.mediaservice.utils.DataSource
import com.example.mediaservice.utils.MediaType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber.d
import javax.inject.Inject

@HiltViewModel
class LocalMusicViewModel @Inject constructor(private val mediaServiceConnection: MediaServiceConnection, private val playlistRepository: PlaylistRepository) : BaseViewModel() {

    private val rootMediaIdExtra = mediaServiceConnection.rootMediaId
    private var currentMediaIdExtra: MediaIdExtra? = null

    private val _mediaItems = MutableLiveData<List<MediaItemUI>>(emptyList())
    val mediaItems : LiveData<List<MediaItemUI>> = _mediaItems

    private val _playlist = MutableLiveData<List<MediaItemUI>>(emptyList())
    val playlist : LiveData<List<MediaItemUI>> = _playlist

    private val subscriptionCallback = object: MediaBrowserCompat.SubscriptionCallback(){
        override fun onChildrenLoaded(parentId: String, children: MutableList<MediaBrowserCompat.MediaItem>) {
            viewModelScope.launch(Dispatchers.Default) {
                val listMediaItemUI = children.map {
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
                _mediaItems.postValue(listMediaItemUI)
                isLoading.postValue(false)
            }
        }
        override fun onError(parentId: String) {
            super.onError(parentId)
            isLoading.postValue(false)
        }
    }

    init{
        startLoadingData(MediaIdExtra.getDataFromString(rootMediaIdExtra))
    }

    fun getCurrentMediaIdExtra(position : Int): Parcelable?{
        return _mediaItems.value?.get(position)?.mediaIdExtra
    }

    fun startLoadingData(mediaIdExtra: MediaIdExtra) {
        if(currentMediaIdExtra != mediaIdExtra) {
            isLoading.postValue(true)
            currentMediaIdExtra = mediaIdExtra
            mediaServiceConnection.subscribe(mediaIdExtra.toString(),subscriptionCallback)
        }

        viewModelScope.launch(Dispatchers.Default) {
            val playlistMediaItem = playlistRepository.findAll(1)
                .map { it.toBrowserMediaItem() }
                .map {
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
            _playlist.postValue(playlistMediaItem)
        }
    }

    override fun onCleared() {
        super.onCleared()
        d("onCleared")
        mediaServiceConnection.unsubscribe(currentMediaIdExtra.toString(),subscriptionCallback)
    }

}
