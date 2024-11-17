package com.example.yp_playlist_maker.media.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.yp_playlist_maker.media.ui.fragment.FavouritesFragment
import com.example.yp_playlist_maker.media.ui.fragment.PlaylistsFragment

class MediaViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> FavouritesFragment.newInstance()
            else -> PlaylistsFragment.newInstance()
        }
    }
}