package com.example.yp_playlist_maker.settings.domain.impl

import com.example.yp_playlist_maker.settings.domain.api.AppThemeInteractor
import com.example.yp_playlist_maker.settings.domain.api.AppThemeRepository
import com.example.yp_playlist_maker.settings.domain.models.AppThemeParams

class AppThemeInteractorImpl(private val repository: AppThemeRepository): AppThemeInteractor {

    override fun saveAppTheme(params: AppThemeParams) {
        repository.saveAppTheme(params)
    }

    override fun getAppTheme(): Boolean {
        return repository.getAppTheme()
    }

}