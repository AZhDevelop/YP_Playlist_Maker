package com.example.yp_playlist_maker.domain.api.app_theme

import com.example.yp_playlist_maker.domain.models.AppThemeParams

interface AppThemeInteractor {

    fun saveAppTheme(params: AppThemeParams)

    fun getAppTheme(): Boolean

}