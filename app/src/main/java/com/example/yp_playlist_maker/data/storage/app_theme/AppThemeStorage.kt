package com.example.yp_playlist_maker.data.storage.app_theme

import com.example.yp_playlist_maker.data.dto.app_theme.AppThemeParamsDto
import com.example.yp_playlist_maker.domain.models.app_theme.AppThemeParams

interface AppThemeStorage {

    fun saveAppTheme(params: AppThemeParamsDto)

    fun getAppTheme(): Boolean

}