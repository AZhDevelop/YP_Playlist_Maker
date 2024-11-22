package com.example.yp_playlist_maker.main.ui

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.databinding.ActivityMainBinding
import com.example.yp_playlist_maker.search.ui.SearchFragment
import com.example.yp_playlist_maker.settings.ui.SettingsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

//        if (savedInstanceState == null) {
//            supportFragmentManager.commit {
//                this.add(R.id.main, SettingsFragment())
//            }
//        }

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                this.add(R.id.main, SearchFragment())
            }
        }

    }
}