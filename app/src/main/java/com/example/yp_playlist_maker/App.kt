package com.example.yp_playlist_maker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

const val THEME_PREFERENCES = "theme_preferences"
const val PREFERENCES_KEY = "app_theme"
const val EMPTY_STRING = ""
const val TRACK_LIST_KEY = "track_list_key"
const val TRACK_KEY = "track_key"

class App: Application() {

    private var darkTheme: Boolean = false

    override fun onCreate() {
        super.onCreate()

        darkTheme = getSharedPreferences(THEME_PREFERENCES, MODE_PRIVATE)
            .getBoolean(PREFERENCES_KEY, false)
        switchTheme(darkTheme)

    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}