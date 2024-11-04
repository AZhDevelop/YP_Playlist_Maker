package com.example.yp_playlist_maker.media.ui

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.yp_playlist_maker.R

class MediaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)

        val backButton = findViewById<ImageView>(R.id.iw_back)

        backButton.setOnClickListener {
            finish()
        }
    }
}