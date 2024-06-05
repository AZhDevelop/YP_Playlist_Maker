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

class MainActivity : AppCompatActivity(), OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /** Реализация нажатия на кнопку "Поиск" с помощью анонимного класса */

        val searchButton = findViewById<Button>(R.id.search)
        val displaySearchIntent = Intent(this, SearchActivity::class.java)
        val searchButtonClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                startActivity(displaySearchIntent)
            }
        }

        searchButton.setOnClickListener(searchButtonClickListener)

        /** Реализация нажатия на кнопку "Медиатека" с помощью лямбда-выражения */

        val mediaButton = findViewById<Button>(R.id.media)
        mediaButton.setOnClickListener {
            val displayMediaIntent = Intent(this, MediaActivity::class.java)
            startActivity(displayMediaIntent)
        }

        /** Реализация нажатия на кнопку "Настройки" с помощью MainActivity */

        val settingsButton = findViewById<Button>(R.id.settings)
        settingsButton.setOnClickListener(this@MainActivity)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.settings -> {
                val displayIntent = Intent(this, SettingsActivity::class.java)
                startActivity(displayIntent)
            }
        }
    }

}