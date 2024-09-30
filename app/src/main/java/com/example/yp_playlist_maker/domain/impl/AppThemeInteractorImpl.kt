package com.example.yp_playlist_maker.domain.impl

import com.example.yp_playlist_maker.domain.api.AppThemeInteractor
import com.example.yp_playlist_maker.domain.api.AppThemeRepository
import com.example.yp_playlist_maker.domain.models.AppThemeParams

class AppThemeInteractorImpl(private val repository: AppThemeRepository): AppThemeInteractor {

    override fun saveSwitcherStatus(params: AppThemeParams) {
        repository.saveSwitcherStatus(params)
    }

    override fun getSwitcherStatus(): Boolean {
        return repository.getSwitcherStatus()
    }

}