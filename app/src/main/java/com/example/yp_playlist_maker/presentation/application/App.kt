package com.example.yp_playlist_maker.presentation.application

import android.app.Application
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.example.yp_playlist_maker.creator.Creator

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

        val getThemeColor = Creator.provideAppThemeInteractor(this)

        darkTheme = getThemeColor.getAppTheme()
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