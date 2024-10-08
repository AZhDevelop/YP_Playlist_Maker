package com.example.yp_playlist_maker.presentation.ui.application

import android.app.Application
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.example.yp_playlist_maker.util.Creator

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
        instance = this

        Creator.init(this)

        val getThemeColor = Creator.provideAppThemeInteractor()

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

    companion object {
        lateinit var instance: App
            private set
    }

}