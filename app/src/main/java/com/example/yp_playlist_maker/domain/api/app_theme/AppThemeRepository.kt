package com.example.yp_playlist_maker.domain.api.app_theme

import com.example.yp_playlist_maker.domain.models.app_theme.AppThemeParams

interface AppThemeRepository {

    fun saveAppTheme(params: AppThemeParams)

    fun getAppTheme(): Boolean

}