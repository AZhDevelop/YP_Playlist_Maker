package com.example.yp_playlist_maker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.btn_search)
        val mediaButton = findViewById<Button>(R.id.btn_media)
        val settingsButton = findViewById<Button>(R.id.btn_settings)

        searchButton.setOnClickListener {
            val displaySearchIntent = Intent(this, SearchActivity::class.java)
            startActivity(displaySearchIntent)
        }

        mediaButton.setOnClickListener {
            val displayMediaIntent = Intent(this, MediaActivity::class.java)
            startActivity(displayMediaIntent)
        }

        settingsButton.setOnClickListener {
            val displaySettingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(displaySettingsIntent)
        }


    }

}