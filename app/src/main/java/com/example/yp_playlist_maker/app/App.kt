package com.example.yp_playlist_maker.app

import android.app.Application
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatDelegate
import com.example.yp_playlist_maker.di.appModule
import com.example.yp_playlist_maker.di.dataModule
import com.example.yp_playlist_maker.di.interactorModule
import com.example.yp_playlist_maker.di.repositoryModule
import com.example.yp_playlist_maker.settings.domain.api.AppThemeRepository
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

fun View.gone() {
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

class App : Application() {

    private var darkTheme: Boolean = false

    override fun onCreate() {
        super.onCreate()
        instance = this

        val getThemeColor: AppThemeRepository by inject<AppThemeRepository>()

        startKoin {
            androidContext(this@App)
            modules(appModule, dataModule, interactorModule, repositoryModule)
        }

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