package com.example.yp_playlist_maker.domain.api

import com.example.yp_playlist_maker.domain.models.AppThemeParams

interface AppThemeInteractor {

    fun saveSwitcherStatus(params: AppThemeParams)

    fun getSwitcherStatus(): Boolean

}