package com.example.yp_playlist_maker.settings.domain.api

import com.example.yp_playlist_maker.settings.domain.models.AppThemeParams

interface AppThemeInteractor {

    fun saveAppTheme(params: AppThemeParams)

    fun getAppTheme(): Boolean

}