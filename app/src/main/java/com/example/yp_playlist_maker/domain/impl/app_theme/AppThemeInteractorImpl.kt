package com.example.yp_playlist_maker.domain.impl.app_theme

import com.example.yp_playlist_maker.domain.api.app_theme.AppThemeInteractor
import com.example.yp_playlist_maker.domain.api.app_theme.AppThemeRepository
import com.example.yp_playlist_maker.domain.models.app_theme.AppThemeParams

class AppThemeInteractorImpl(private val repository: AppThemeRepository): AppThemeInteractor {

    override fun saveAppTheme(params: AppThemeParams) {
        repository.saveAppTheme(params)
    }

    override fun getAppTheme(): Boolean {
        return repository.getAppTheme()
    }

}