package com.example.yp_playlist_maker

import android.content.Context
import com.example.yp_playlist_maker.data.AppThemeImpl
import com.example.yp_playlist_maker.domain.api.AppThemeInteractor
import com.example.yp_playlist_maker.domain.api.AppThemeRepository
import com.example.yp_playlist_maker.domain.impl.AppThemeInteractorImpl

object Creator {

    private fun getAppThemeRepository(context: Context): AppThemeRepository {
        return AppThemeImpl(context)
    }

    fun provideAppThemeInteractor(context: Context): AppThemeInteractor {
        return AppThemeInteractorImpl(getAppThemeRepository(context))
    }
}