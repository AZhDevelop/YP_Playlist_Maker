package com.example.yp_playlist_maker.media.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.yp_playlist_maker.databinding.ActivityMediaBinding
import com.google.android.material.tabs.TabLayoutMediator

class MediaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediaBinding
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.iwBack.setOnClickListener {
            finish()
        }

        binding.viewPager.adapter = MediaViewPagerAdapter(supportFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when(position) {
                0 -> tab.text = FAVOURITE_TRACKS
                else -> tab.text = PLAYLISTS
            }
        }
        tabMediator.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }

    companion object {
        private const val FAVOURITE_TRACKS = "Избранные треки"
        private const val PLAYLISTS = "Плейлисты"
    }
}