package com.example.baseproject.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.baseproject.ui.home.addSongPlaylist.listSongPager.ListSongPagerFragment

class AddSongPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager,lifecycle) {
    private val mNumOfTabs = 2

    override fun getItemCount(): Int {
       return mNumOfTabs
    }

    override fun createFragment(position: Int): Fragment {
        return ListSongPagerFragment()
    }
}