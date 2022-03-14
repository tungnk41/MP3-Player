package com.example.baseproject.navigation

import android.os.Bundle
import com.example.core.navigationComponent.BaseNavigator

interface HomeNavigation : BaseNavigator {

    fun openLocalMusicScreenToSongScreen(bundle: Bundle? = null)
}