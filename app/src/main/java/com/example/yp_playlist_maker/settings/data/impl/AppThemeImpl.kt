package com.example.yp_playlist_maker.settings.data.impl

import android.content.Context
import com.example.yp_playlist_maker.settings.domain.api.AppThemeRepository
import com.example.yp_playlist_maker.settings.domain.models.AppThemeParams

class AppThemeImpl(context: Context) : AppThemeRepository {

    private val sharedPreferences =
        context.getSharedPreferences(THEME_PREFERENCES, Context.MODE_PRIVATE)

    override fun saveAppTheme(params: AppThemeParams) {
        sharedPreferences.edit()
            .putBoolean(THEME_PREFERENCES_KEY, params.switcherStatus)
            .apply()
    }

    override fun getAppTheme(): Boolean {
        return sharedPreferences.getBoolean(THEME_PREFERENCES_KEY, false)
    }

    companion object {
        private const val THEME_PREFERENCES: String = "theme_preferences"
        private const val THEME_PREFERENCES_KEY: String = "theme_preferences_key"
    }
}