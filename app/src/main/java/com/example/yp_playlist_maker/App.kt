package com.example.yp_playlist_maker

import android.app.Application
import android.view.View
import androidx.appcompat.app.AppCompatDelegate

const val THEME_PREFERENCES: String = "theme_preferences"
const val PREFERENCES_KEY: String = "app_theme"
const val EMPTY_STRING: String = ""
const val TRACK_LIST_KEY: String = "track_list_key"
const val TRACK_KEY: String = "track_key"
const val TRACK_HISTORY_SIZE: Int = 10
const val ZERO_INDEX: Int = 0

fun View.gone() {
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

class App : Application() {

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