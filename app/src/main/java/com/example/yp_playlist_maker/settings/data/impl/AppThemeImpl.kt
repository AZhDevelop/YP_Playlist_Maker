package com.example.yp_playlist_maker.settings.data.impl

import android.content.SharedPreferences
import com.example.yp_playlist_maker.settings.domain.api.AppThemeRepository
import com.example.yp_playlist_maker.settings.domain.models.AppThemeParams
import com.example.yp_playlist_maker.util.Constants

class AppThemeImpl(private val sharedPreferences: SharedPreferences) : AppThemeRepository {

    override fun saveAppTheme(params: AppThemeParams) {
        sharedPreferences.edit()
            .putBoolean(Constants.THEME_PREFERENCES_KEY, params.switcherStatus)
            .apply()
    }

    override fun getAppTheme(): Boolean {
        return sharedPreferences.getBoolean(Constants.THEME_PREFERENCES_KEY, false)
    }

    companion object {

    }
}