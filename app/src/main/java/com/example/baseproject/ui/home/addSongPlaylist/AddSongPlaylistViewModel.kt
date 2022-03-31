package com.example.baseproject.ui.home.addSongPlaylist

import android.net.Uri
import android.provider.MediaStore
import android.support.v4.media.MediaBrowserCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.baseproject.model.MediaItemUI
import com.example.core.base.BaseViewModel
import com.example.mediaservice.repository.SongRepository.SongRepository
import com.example.mediaservice.repository.models.MediaIdExtra
import com.example.mediaservice.repository.models.Song
import com.example.mediaservice.utils.DataSource
import com.example.mediaservice.utils.MediaType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import timber.log.Timber.d
import javax.inject.Inject

@HiltViewModel
class AddSongPlaylistViewModel @Inject constructor(private val songRepository: SongRepository): BaseViewModel() {

    var mediaIdExtra: MediaIdExtra? = null

    private var _searchLocalSong : MutableLiveData<List<MediaItemUI>> = MutableLiveData(listOf())
    val searchLocalSong : LiveData<List<MediaItemUI>> = _searchLocalSong

    private var _searchRemoteSong : MutableLiveData<List<MediaItemUI>> = MutableLiveData(listOf())
    val searchRemoteSong : LiveData<List<MediaItemUI>> = _searchRemoteSong

    private var _listSelectedSong : MutableLiveData<MutableList<Pair<Long,Int>>> = MutableLiveData(mutableListOf()) // SongId, datasource
    val listSelectedSong : LiveData<MutableList<Pair<Long,Int>>> = _listSelectedSong

    private var debounceJob: Job? = null
    private val debounceScope = CoroutineScope(Dispatchers.Main)

    private var allLocalSong: List<MediaItemUI>? = null
    private var allRemoteSong: List<MediaItemUI>? = null

    init {
        loadAllSong()
    }

    private fun loadAllSong() {
        viewModelScope.launch(Dispatchers.Default) {
            isLoading.postValue(true)
            allLocalSong = songRepository
                .findAll(dataSource = DataSource.LOCAL)
                .map {
                    val mediaIdExtra = MediaIdExtra(parentMediaType = MediaType.TYPE_PLAYLIST,mediaType = MediaType.TYPE_SONG, id = it.id, dataSource = DataSource.LOCAL)
                    val id: Long= it.id
                    val title: String = it.title
                    val subTitle: String = it.artist
                    val iconUri: Uri = Uri.parse(it.iconUri) ?: Uri.EMPTY
                    val isBrowsable: Boolean = false
                    val mediaType = MediaType.TYPE_SONG
                    val dataSource = DataSource.LOCAL
                    MediaItemUI(mediaIdExtra = mediaIdExtra,id = id, title = title, subTitle = subTitle , iconUri = iconUri, isBrowsable = isBrowsable, dataSource = dataSource, mediaType = mediaType)
                }
            allRemoteSong = songRepository
                .findAll(dataSource = DataSource.REMOTE)
                .map {
                    val mediaIdExtra = MediaIdExtra(parentMediaType = MediaType.TYPE_PLAYLIST,mediaType = MediaType.TYPE_SONG, id = it.id, dataSource = DataSource.REMOTE)
                    val id: Long= it.id
                    val title: String = it.title
                    val subTitle: String = it.artist
                    val iconUri: Uri = Uri.parse(it.iconUri) ?: Uri.EMPTY
                    val isBrowsable: Boolean = false
                    val mediaType = MediaType.TYPE_SONG
                    val dataSource = DataSource.REMOTE
                    MediaItemUI(mediaIdExtra = mediaIdExtra,id = id, title = title, subTitle = subTitle , iconUri = iconUri, isBrowsable = isBrowsable, dataSource = dataSource, mediaType = mediaType)
                }
            _searchLocalSong.postValue(allLocalSong)
            _searchRemoteSong.postValue(allRemoteSong)
            isLoading.postValue(false)
        }
    }

    fun searchSong(title: String) {
        viewModelScope.launch {
                debounceJob?.cancel()
                debounceJob = debounceScope.launch {
                    if (title.isNotEmpty()) {
                        delay(500)
                        isLoading.postValue(true)
                        val searchLocalSongData = async {
                            songRepository
                                .searchLocalSong(title)
                                .map { mapToMediaItemUI(it.toBrowserMediaItem(parentMediaType = MediaType.TYPE_PLAYLIST)) }
                        }
                        val searchRemoteSongData = async {
                            songRepository
                                .searchRemoteSong(title)
                                .map { mapToMediaItemUI(it.toBrowserMediaItem(parentMediaType = MediaType.TYPE_PLAYLIST)) }
                        }
                        awaitAll(searchLocalSongData,searchRemoteSongData)
                        _searchLocalSong.postValue(searchLocalSongData.await())
                        _searchRemoteSong.postValue(searchRemoteSongData.await())
                        isLoading.postValue(false)
                    }
                    else {
                        _searchLocalSong.postValue(allLocalSong)
                        _searchRemoteSong.postValue(allRemoteSong)
                    }
                }
        }
    }

    fun updateSelectedItem(songId: Long, dataSource: Int, isSelected: Boolean) {
        if(isSelected) {
            _listSelectedSong.value?.add(Pair(songId,dataSource))
        }
        else{
            _listSelectedSong.value?.remove(Pair(songId,dataSource))
        }
        _listSelectedSong.value = _listSelectedSong.value
    }

    fun clearAllSelectedItem() {
            _searchLocalSong.value?.forEach {
                it.isSelected = false
            }
            _searchRemoteSong.value?.forEach {
                it.isSelected = false
            }
            _listSelectedSong.value?.clear()
            _searchLocalSong.value = _searchLocalSong.value
            _searchRemoteSong.value = _searchRemoteSong.value
            _listSelectedSong.value = _listSelectedSong.value
    }

    fun saveSongToPlaylist() {
        viewModelScope.launch {
            val playlistId = mediaIdExtra?.id ?: -1
            _listSelectedSong.value?.forEach {
                d("saveSongToPlaylist " + it.toString())
                songRepository.saveSongToPlaylist(playlistId,it.first,it.second)
            }
            withContext(Dispatchers.Main){
                clearAllSelectedItem()
            }
        }
    }

    private fun mapToMediaItemUI(browserMediaItem: MediaBrowserCompat.MediaItem) : MediaItemUI{
        val mediaIdExtra = MediaIdExtra.getDataFromString(browserMediaItem.mediaId ?: "")
        val id: Long= mediaIdExtra.id ?: -1
        val title: String = browserMediaItem.description.title.toString()
        val subTitle: String = browserMediaItem.description.subtitle.toString()
        val iconUri: Uri = browserMediaItem.description.iconUri ?: Uri.EMPTY
        val isBrowsable: Boolean = browserMediaItem.flags.equals(MediaBrowserCompat.MediaItem.FLAG_BROWSABLE)
        val mediaType = mediaIdExtra.mediaType ?: -1
        val dataSource = mediaIdExtra.dataSource
        return MediaItemUI(mediaIdExtra = mediaIdExtra,id = id, title = title, subTitle = subTitle , iconUri = iconUri, isBrowsable = isBrowsable, dataSource = dataSource, mediaType = mediaType)
    }

    private fun <T> debounce(
        waitMs: Long = 300L,
        coroutineScope: CoroutineScope,
        destinationFunction: (T) -> Unit
    ): (T) -> Unit {
        var debounceJob: Job? = null
        return { param: T ->
            debounceJob?.cancel()
            debounceJob = coroutineScope.launch {
                delay(waitMs)
                destinationFunction(param)
            }
        }
    }

}