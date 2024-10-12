package com.example.yp_playlist_maker.creator

import android.content.Context
import com.example.yp_playlist_maker.settings.data.impl.AppThemeImpl
import com.example.yp_playlist_maker.player.data.impl.PlayTrackRepositoryImpl
import com.example.yp_playlist_maker.search.data.impl.SearchHistoryRepositoryImpl
import com.example.yp_playlist_maker.search.data.impl.TrackRepositoryImpl
import com.example.yp_playlist_maker.search.data.network.RetrofitNetworkClient
import com.example.yp_playlist_maker.settings.domain.api.AppThemeInteractor
import com.example.yp_playlist_maker.player.domain.api.PlayTrackInteractor
import com.example.yp_playlist_maker.search.domain.api.SearchHistoryInteractor
import com.example.yp_playlist_maker.search.domain.api.TrackInteractor
import com.example.yp_playlist_maker.settings.domain.api.AppThemeRepository
import com.example.yp_playlist_maker.player.domain.api.PlayTrackRepository
import com.example.yp_playlist_maker.search.domain.api.SearchHistoryRepository
import com.example.yp_playlist_maker.search.domain.api.TrackRepository
import com.example.yp_playlist_maker.settings.domain.impl.AppThemeInteractorImpl
import com.example.yp_playlist_maker.player.domain.impl.PlayTrackInteractorImpl
import com.example.yp_playlist_maker.search.domain.impl.SearchHistoryInteractorImpl
import com.example.yp_playlist_maker.search.domain.impl.TrackInteractorImpl
import com.example.yp_playlist_maker.sharing.data.impl.ContactSupportRepositoryImpl
import com.example.yp_playlist_maker.sharing.data.impl.ShareAppRepositoryImpl
import com.example.yp_playlist_maker.sharing.domain.api.interactor.ContactSupportInteractor
import com.example.yp_playlist_maker.sharing.domain.api.interactor.ShareAppInteractor
import com.example.yp_playlist_maker.sharing.domain.api.repository.ContactSupportRepository
import com.example.yp_playlist_maker.sharing.domain.api.repository.ShareAppRepository
import com.example.yp_playlist_maker.sharing.domain.impl.ContactSupportInteractorImpl
import com.example.yp_playlist_maker.sharing.domain.impl.ShareAppInteractorImpl

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

    // Поделиться приложением
    private fun getShareAppRepository() : ShareAppRepository {
        return ShareAppRepositoryImpl(appContext)
    }

    fun provideShareAppInteractor() : ShareAppInteractor {
        return ShareAppInteractorImpl(getShareAppRepository())
    }

    // Обратиться в поддержку
    private fun getContactSupportRepository() : ContactSupportRepository {
        return ContactSupportRepositoryImpl(appContext)
    }

    fun provideContactSupportInteractor() : ContactSupportInteractor {
        return ContactSupportInteractorImpl(getContactSupportRepository())
    }

}