package com.example.yp_playlist_maker.data

import android.content.Context
import com.example.yp_playlist_maker.domain.api.AppThemeRepository
import com.example.yp_playlist_maker.domain.models.AppThemeParams

const val THEME_PREFERENCES: String = "theme_preferences"
const val THEME_PREFERENCES_KEY: String = "theme_preferences_key"

class AppThemeImpl(context: Context) : AppThemeRepository {

    private val sharedPreferences = context.getSharedPreferences(THEME_PREFERENCES, Context.MODE_PRIVATE)

    override fun saveSwitcherStatus(params: AppThemeParams) {
        sharedPreferences.edit()
            .putBoolean(THEME_PREFERENCES_KEY, params.switcherStatus)
            .apply()
    }

    override fun getSwitcherStatus(): Boolean {
        return sharedPreferences.getBoolean(THEME_PREFERENCES_KEY, false)
    }
}