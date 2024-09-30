package com.example.yp_playlist_maker

import android.content.Context
import com.example.yp_playlist_maker.data.impl.app_theme.AppThemeImpl
import com.example.yp_playlist_maker.data.storage.app_theme.SharedPrefsAppThemeStorage
import com.example.yp_playlist_maker.domain.api.app_theme.AppThemeInteractor
import com.example.yp_playlist_maker.domain.api.app_theme.AppThemeRepository
import com.example.yp_playlist_maker.domain.impl.app_theme.AppThemeInteractorImpl

object Creator {

    private fun getAppThemeRepository(context: Context): AppThemeRepository {
        return AppThemeImpl(SharedPrefsAppThemeStorage(context))
    }

    fun provideAppThemeInteractor(context: Context): AppThemeInteractor {
        return AppThemeInteractorImpl(getAppThemeRepository(context))
    }
}