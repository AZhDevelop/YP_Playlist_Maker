package com.example.yp_playlist_maker.data.impl.app_theme

import com.example.yp_playlist_maker.data.dto.app_theme.AppThemeParamsDto
import com.example.yp_playlist_maker.data.storage.app_theme.AppThemeStorage
import com.example.yp_playlist_maker.domain.api.app_theme.AppThemeRepository
import com.example.yp_playlist_maker.domain.models.app_theme.AppThemeParams

class AppThemeImpl(private val appThemeStorage: AppThemeStorage) : AppThemeRepository {

    override fun saveAppTheme(params: AppThemeParams) {

        val appThemeParamsDto = AppThemeParamsDto(params.switcherStatus)
        appThemeStorage.saveAppTheme(appThemeParamsDto)
    }

    override fun getAppTheme(): Boolean {
        return appThemeStorage.getAppTheme()
    }
}