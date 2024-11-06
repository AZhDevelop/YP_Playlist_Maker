package com.example.yp_playlist_maker.creator

import android.content.Context
import com.example.yp_playlist_maker.player.data.impl.PlayTrackRepositoryImpl
import com.example.yp_playlist_maker.player.domain.api.PlayTrackInteractor
import com.example.yp_playlist_maker.player.domain.api.PlayTrackRepository
import com.example.yp_playlist_maker.player.domain.impl.PlayTrackInteractorImpl
import com.example.yp_playlist_maker.settings.data.impl.AppThemeImpl
import com.example.yp_playlist_maker.settings.domain.api.AppThemeInteractor
import com.example.yp_playlist_maker.settings.domain.api.AppThemeRepository
import com.example.yp_playlist_maker.settings.domain.impl.AppThemeInteractorImpl

object Creator {

    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    // Тема приложения
    private fun getAppThemeRepository(): AppThemeRepository {
        return AppThemeImpl(appContext)
    }

    fun provideAppThemeInteractor(): AppThemeInteractor {
        return AppThemeInteractorImpl(getAppThemeRepository())
    }

    // Взаимодействие с плеером
    private fun getPlayTrackRepository() : PlayTrackRepository {
        return PlayTrackRepositoryImpl()
    }

    fun providePlayTrackInteractor() : PlayTrackInteractor {
        return PlayTrackInteractorImpl(getPlayTrackRepository())
    }

}