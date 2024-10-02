package com.example.yp_playlist_maker.domain.api.repository

import com.example.yp_playlist_maker.domain.models.AppThemeParams

interface AppThemeRepository {

    fun saveAppTheme(params: AppThemeParams)

    fun getAppTheme(): Boolean

}