package com.example.yp_playlist_maker

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class AudioPlayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audioplayer)

        val backButton = findViewById<ImageView>(R.id.iw_back)

        backButton.setOnClickListener {
            finish()
        }
    }
}