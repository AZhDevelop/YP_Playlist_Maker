package com.example.yp_playlist_maker.util

import android.content.Context
import com.example.yp_playlist_maker.data.impl.AppThemeImpl
import com.example.yp_playlist_maker.data.impl.PlayTrackRepositoryImpl
import com.example.yp_playlist_maker.data.impl.SearchHistoryRepositoryImpl
import com.example.yp_playlist_maker.data.impl.TrackRepositoryImpl
import com.example.yp_playlist_maker.data.network.RetrofitNetworkClient
import com.example.yp_playlist_maker.domain.api.interactor.AppThemeInteractor
import com.example.yp_playlist_maker.domain.api.interactor.PlayTrackInteractor
import com.example.yp_playlist_maker.domain.api.interactor.SearchHistoryInteractor
import com.example.yp_playlist_maker.domain.api.interactor.TrackInteractor
import com.example.yp_playlist_maker.domain.api.repository.AppThemeRepository
import com.example.yp_playlist_maker.domain.api.repository.PlayTrackRepository
import com.example.yp_playlist_maker.domain.api.repository.SearchHistoryRepository
import com.example.yp_playlist_maker.domain.api.repository.TrackRepository
import com.example.yp_playlist_maker.domain.impl.AppThemeInteractorImpl
import com.example.yp_playlist_maker.domain.impl.PlayTrackInteractorImpl
import com.example.yp_playlist_maker.domain.impl.SearchHistoryInteractorImpl
import com.example.yp_playlist_maker.domain.impl.TrackInteractorImpl

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

    // Поиск трека, работа с сетью
    private fun getTrackRepository() : TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient(appContext))
    }

    fun provideTrackInteractor() : TrackInteractor {
        return TrackInteractorImpl(getTrackRepository())
    }

    // История поиска треков
    private fun getSearchHistoryRepository() : SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(appContext)
    }

    fun provideSeacrhHistoryInteractor() : SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(getSearchHistoryRepository())
    }

    // Взаимодействие с плеером
    private fun getPlayTrackRepository() : PlayTrackRepository {
        return PlayTrackRepositoryImpl()
    }

    fun providePlayTrackInteractor() : PlayTrackInteractor {
        return PlayTrackInteractorImpl(getPlayTrackRepository())
    }

}