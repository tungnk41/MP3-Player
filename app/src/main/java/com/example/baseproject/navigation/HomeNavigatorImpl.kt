package com.example.baseproject.navigation

import android.os.Bundle
import com.example.baseproject.R
import com.example.core.navigationComponent.BaseNavigatorImpl
import javax.inject.Inject

class HomeNavigatorImpl @Inject constructor() : BaseNavigatorImpl(), HomeNavigation {

    override fun openLocalMusicScreenToSongScreen(bundle: Bundle?) {
        openScreen(R.id.action_localMusicFragment_toSongFragment, null)
    }

}