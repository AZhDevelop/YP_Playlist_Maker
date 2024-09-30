package com.example.yp_playlist_maker.data.storage.app_theme

import android.content.Context
import com.example.yp_playlist_maker.data.dto.app_theme.AppThemeParamsDto
import com.example.yp_playlist_maker.domain.models.app_theme.AppThemeParams

const val THEME_PREFERENCES: String = "theme_preferences"
const val THEME_PREFERENCES_KEY: String = "theme_preferences_key"

class SharedPrefsAppThemeStorage(context: Context): AppThemeStorage {

    private val sharedPreferences = context.getSharedPreferences(THEME_PREFERENCES, Context.MODE_PRIVATE)

    override fun saveAppTheme(params: AppThemeParamsDto) {
        sharedPreferences.edit()
            .putBoolean(THEME_PREFERENCES_KEY, params.switcherStatus)
            .apply()
    }

    override fun getAppTheme(): Boolean {
        return sharedPreferences.getBoolean(THEME_PREFERENCES_KEY, false)
    }
}