package com.example.yp_playlist_maker.creator

import android.content.Context
import android.net.ConnectivityManager
import com.example.yp_playlist_maker.player.data.impl.PlayTrackRepositoryImpl
import com.example.yp_playlist_maker.player.domain.api.PlayTrackInteractor
import com.example.yp_playlist_maker.player.domain.api.PlayTrackRepository
import com.example.yp_playlist_maker.player.domain.impl.PlayTrackInteractorImpl
import com.example.yp_playlist_maker.search.data.impl.SearchHistoryRepositoryImpl
import com.example.yp_playlist_maker.search.data.impl.TrackRepositoryImpl
import com.example.yp_playlist_maker.search.data.network.RetrofitNetworkClient
import com.example.yp_playlist_maker.search.domain.api.SearchHistoryInteractor
import com.example.yp_playlist_maker.search.domain.api.SearchHistoryRepository
import com.example.yp_playlist_maker.search.domain.api.TrackInteractor
import com.example.yp_playlist_maker.search.domain.api.TrackRepository
import com.example.yp_playlist_maker.search.domain.impl.SearchHistoryInteractorImpl
import com.example.yp_playlist_maker.search.domain.impl.TrackInteractorImpl
import com.example.yp_playlist_maker.settings.data.impl.AppThemeImpl
import com.example.yp_playlist_maker.settings.domain.api.AppThemeInteractor
import com.example.yp_playlist_maker.settings.domain.api.AppThemeRepository
import com.example.yp_playlist_maker.settings.domain.impl.AppThemeInteractorImpl

object Creator {

    private lateinit var appContext: Context
    private lateinit var connectivityManager: ConnectivityManager

    fun init(context: Context) {
        appContext = context.applicationContext
        connectivityManager = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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
        return TrackRepositoryImpl(RetrofitNetworkClient(connectivityManager))
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