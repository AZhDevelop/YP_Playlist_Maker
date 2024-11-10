package com.example.yp_playlist_maker.di

import com.example.yp_playlist_maker.player.domain.api.PlayTrackInteractor
import com.example.yp_playlist_maker.player.domain.impl.PlayTrackInteractorImpl
import com.example.yp_playlist_maker.search.domain.api.SearchHistoryInteractor
import com.example.yp_playlist_maker.search.domain.api.TrackInteractor
import com.example.yp_playlist_maker.search.domain.impl.SearchHistoryInteractorImpl
import com.example.yp_playlist_maker.search.domain.impl.TrackInteractorImpl
import com.example.yp_playlist_maker.settings.domain.api.AppThemeInteractor
import com.example.yp_playlist_maker.settings.domain.impl.AppThemeInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    //Поиск треков в сети
    factory<TrackInteractor> {
        TrackInteractorImpl(
            repository = get()
        )
    }

    //История поиска треков
    factory<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(
            repository = get()
        )
    }

    //Тема приложения
    factory<AppThemeInteractor> {
        AppThemeInteractorImpl(
            repository = get()
        )
    }

    //Аудиоплеер
    factory<PlayTrackInteractor> {
        PlayTrackInteractorImpl(
            repository = get()
        )
    }

}