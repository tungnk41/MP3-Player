package com.example.baseproject.ui.home.addSong

import android.support.v4.media.MediaMetadataCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.core.base.BaseViewModel
import com.example.mediaservice.repository.SongRepository.SongRepository
import com.example.mediaservice.repository.models.Song
import com.example.mediaservice.utils.DataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddSongViewModel @Inject constructor(private val songRepository: SongRepository): BaseViewModel() {

    private var allLocalSong = listOf<Song>()

    private var _searchLocalSong : MutableLiveData<List<Song>> = MutableLiveData(listOf())
    val searchLocalSong : LiveData<List<Song>> = _searchLocalSong

    private var _searchRemoteSong : MutableLiveData<List<Song>> = MutableLiveData(listOf())
    val searchRemoteSong : LiveData<List<Song>> = _searchRemoteSong


    init {
        loadAllSongLocal()
    }

    private fun loadAllSongLocal() {
        viewModelScope.launch {
            allLocalSong = songRepository.findAll(dataSource = DataSource.LOCAL)
        }
    }

    private fun searchSong(songTitle: String) {

    }


}