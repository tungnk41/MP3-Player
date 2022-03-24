package com.example.baseproject.ui.home

import android.support.v4.media.MediaMetadataCompat
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.baseproject.R
import com.example.baseproject.model.MediaItemUI
import com.example.core.base.BaseViewModel
import com.example.mediaservice.extensions.EMPTY_MEDIA_METADATA_COMPAT
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(): BaseViewModel() {

    private val _mIsShowBottomController = MutableLiveData<Boolean>(false)
    val mIsShowBottomController : LiveData<Boolean> = _mIsShowBottomController

    private val _mIsShowBottomNav = MutableLiveData<Boolean>(false)
    val mIsShowBottomNav : LiveData<Boolean> = _mIsShowBottomNav

    private val _mIsShowToolbar = MutableLiveData<Boolean>(false)
    val mIsShowToolbar : LiveData<Boolean> = _mIsShowToolbar

    private var mMediaMetadata = EMPTY_MEDIA_METADATA_COMPAT
    private var mIsCanShowBottomController : Boolean = false

    fun setCurrentMetadata(mediaMetadataCompat: MediaMetadataCompat) {
        mMediaMetadata = mediaMetadataCompat
        reCheckConditionToShowBottomController()
    }

    fun handleConditionScreenId(screenId: Int) {
        var isShowToolbar = true
        var isShowBottomNav = true
        var isCanShowBottomController = true
        when(screenId) {
            R.id.createPlaylistFragment,R.id.addSongPlaylistFragment -> {
                isShowBottomNav = false
                isCanShowBottomController= false
            }
            R.id.tabOnlineMusicFragment,R.id.tabLocalMusicFragment -> {
                isShowToolbar = false
            }
        }
        _mIsShowBottomNav.value = isShowBottomNav
        _mIsShowToolbar.value = isShowToolbar
        mIsCanShowBottomController = isCanShowBottomController
        reCheckConditionToShowBottomController()
    }

    fun reCheckConditionToShowBottomController() {
        _mIsShowBottomController.value = (mIsCanShowBottomController && mMediaMetadata != EMPTY_MEDIA_METADATA_COMPAT)
    }
    
}