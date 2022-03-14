package com.example.baseproject.ui.home.tabOnlineMusic

import android.net.Uri
import android.os.Parcelable
import android.support.v4.media.MediaBrowserCompat
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
class OnlineMusicViewModel @Inject constructor(private val mediaServiceConnection: MediaServiceConnection) : BaseViewModel() {

    private val mediaIdExtra = MediaIdExtra(mediaType = MediaType.TYPE_ALL_SONGS, dataSource = DataSource.REMOTE)

    private val _allSongsCollapse = MutableLiveData<List<MediaItemUI>>(emptyList())
    val allSongsCollapse : LiveData<List<MediaItemUI>> = _allSongsCollapse

    private val subscriptionCallback = object: MediaBrowserCompat.SubscriptionCallback(){
        override fun onChildrenLoaded(parentId: String, children: MutableList<MediaBrowserCompat.MediaItem>) {
            viewModelScope.launch(Dispatchers.Default) {
                val listMediaItemExtra = children.take(5).map {
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
                _allSongsCollapse.postValue(listMediaItemExtra)
            }
        }
    }

    fun getCurrentMediaIdExtra(position : Int): Parcelable?{
        return allSongsCollapse.value?.get(position)?.mediaIdExtra
    }
    fun startLoadingData() {
        mediaServiceConnection.subscribe(mediaIdExtra.toString(),subscriptionCallback)
    }

    override fun onCleared() {
        super.onCleared()
        mediaServiceConnection.unsubscribe(mediaIdExtra.toString(),subscriptionCallback)
    }
}
