package com.example.baseproject.ui.home.tabOnlineMusic

import android.net.Uri
import android.os.Parcelable
import android.support.v4.media.MediaBrowserCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.baseproject.model.MediaItemUI
import com.example.baseproject.model.MediaOnlineItem
import com.example.core.base.BaseViewModel
import com.example.mediaservice.MediaServiceConnection
import com.example.mediaservice.extensions.toBrowserMediaItem
import com.example.mediaservice.repository.AlbumRepository.AlbumRepository
import com.example.mediaservice.repository.ArtistRepository.ArtistRepository
import com.example.mediaservice.repository.GenreRepository.GenreRepository
import com.example.mediaservice.repository.SongRepository.SongRepository
import com.example.mediaservice.repository.models.MediaIdExtra
import com.example.mediaservice.utils.DataSource
import com.example.mediaservice.utils.MediaType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import timber.log.Timber.d
import javax.inject.Inject

@HiltViewModel
class OnlineMusicViewModel @Inject constructor(
    private val mediaServiceConnection: MediaServiceConnection,
    private val songRepository: SongRepository,
    private val albumRepository: AlbumRepository,
    private val artistRepository: ArtistRepository,
    private val genreRepository: GenreRepository
    ) : BaseViewModel() {

    private var currentMediaIdExtra: MediaIdExtra? = null

    private val _listMediaOnlineSection = MutableLiveData<List<MediaOnlineItem>>(emptyList())
    val listMediaOnlineSection : LiveData<List<MediaOnlineItem>> = _listMediaOnlineSection

    init {
        startLoadingData()
    }

    fun startLoadingData() {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading.postValue(true)
            val listMediaItemUISong = async {
                songRepository
                    .findAll(dataSource = DataSource.REMOTE)
                    .take(7)
                    .map { mapToMediaItemUI( it.toBrowserMediaItem(mediaType = MediaType.TYPE_SONG, parentMediaType = MediaType.TYPE_ALL_SONGS, dataSource = DataSource.REMOTE) ) }
            }
            val listMediaItemUIAlbum = async {
                albumRepository
                    .findAll(dataSource = DataSource.REMOTE)
                    .take(7)
                    .map { mapToMediaItemUI( it.toBrowserMediaItem(mediaType = MediaType.TYPE_ALBUM,parentMediaType = MediaType.TYPE_ALL_ALBUMS, dataSource = DataSource.REMOTE) ) }
            }
            val listMediaItemUIArtist = async {
                artistRepository
                    .findAll(dataSource = DataSource.REMOTE)
                    .take(7)
                    .map { mapToMediaItemUI( it.toBrowserMediaItem(mediaType = MediaType.TYPE_ARTIST,parentMediaType = MediaType.TYPE_ALL_ARTISTS, dataSource = DataSource.REMOTE) ) }
            }
            val listMediaItemUIGenre = async {
                genreRepository
                    .findAll(dataSource = DataSource.REMOTE)
                    .take(7)
                    .map { mapToMediaItemUI( it.toBrowserMediaItem(mediaType = MediaType.TYPE_GENRE,parentMediaType = MediaType.TYPE_ALL_GENRES, dataSource = DataSource.REMOTE) ) }
            }
            awaitAll(listMediaItemUISong,listMediaItemUIAlbum,listMediaItemUIArtist,listMediaItemUIGenre)
            _listMediaOnlineSection.postValue(
                listOf(
                    MediaOnlineItem(title = "Songs", listChild = listMediaItemUISong.await()),
                    MediaOnlineItem(title = "Albums", listChild = listMediaItemUIAlbum.await()),
                    MediaOnlineItem(title = "Artists", listChild = listMediaItemUIArtist.await()),
                    MediaOnlineItem(title = "Genres", listChild = listMediaItemUIGenre.await()),
                )
            )
            isLoading.postValue(false)
        }
    }

    fun mapToMediaItemUI(browserMediaItem: MediaBrowserCompat.MediaItem) : MediaItemUI{
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

    override fun onCleared() {
        super.onCleared()
    }
}
