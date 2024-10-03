package com.example.yp_playlist_maker

import android.content.Context
import com.example.yp_playlist_maker.data.impl.AppThemeImpl
import com.example.yp_playlist_maker.data.impl.SearchHistoryRepositoryImpl
import com.example.yp_playlist_maker.data.impl.TrackRepositoryImpl
import com.example.yp_playlist_maker.data.network.NetworkClient
import com.example.yp_playlist_maker.data.network.RetrofitNetworkClient
import com.example.yp_playlist_maker.domain.api.interactor.AppThemeInteractor
import com.example.yp_playlist_maker.domain.api.interactor.PlayTrackInteractor
import com.example.yp_playlist_maker.domain.api.interactor.SearchHistoryInteractor
import com.example.yp_playlist_maker.domain.api.interactor.TrackInteractor
import com.example.yp_playlist_maker.domain.api.repository.AppThemeRepository
import com.example.yp_playlist_maker.domain.api.repository.SearchHistoryRepository
import com.example.yp_playlist_maker.domain.api.repository.TrackRepository
import com.example.yp_playlist_maker.domain.impl.AppThemeInteractorImpl
import com.example.yp_playlist_maker.domain.impl.PlayTrackInteractorImpl
import com.example.yp_playlist_maker.domain.impl.SearchHistoryInteractorImpl
import com.example.yp_playlist_maker.domain.impl.TrackInteractorImpl
import com.example.yp_playlist_maker.domain.models.PlayerParams
import com.example.yp_playlist_maker.domain.use_case.PlayTrackUseCase

object Creator {

    // Тема приложения
    private fun getAppThemeRepository(context: Context): AppThemeRepository {
        return AppThemeImpl(context)
    }

    fun provideAppThemeInteractor(context: Context): AppThemeInteractor {
        return AppThemeInteractorImpl(getAppThemeRepository(context))
    }

    // Поиск трека, работа с сетью
    private fun getTrackRepository() : TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTrackInteractor() : TrackInteractor {
        return TrackInteractorImpl(getTrackRepository())
    }

    // История поиска треков
    private fun getSearchHistoryRepository(context: Context) : SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(context)
    }

    fun provideSeacrhHistoryInteractor(context: Context) : SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(getSearchHistoryRepository(context))
    }

    // Взаимодействие с плеером
    private fun getPlayTrackInteractor(playerParams: PlayerParams) : PlayTrackInteractor {
        return PlayTrackInteractorImpl(playerParams)
    }

    fun providePlayTrackUseCase(playerParams: PlayerParams) : PlayTrackUseCase {
        return PlayTrackUseCase(getPlayTrackInteractor(playerParams))
    }

}