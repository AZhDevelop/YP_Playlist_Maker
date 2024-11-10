package com.example.yp_playlist_maker.di

import com.example.yp_playlist_maker.player.data.impl.PlayTrackRepositoryImpl
import com.example.yp_playlist_maker.player.domain.api.PlayTrackRepository
import com.example.yp_playlist_maker.search.data.impl.SearchHistoryRepositoryImpl
import com.example.yp_playlist_maker.search.data.impl.TrackRepositoryImpl
import com.example.yp_playlist_maker.search.domain.api.SearchHistoryRepository
import com.example.yp_playlist_maker.search.domain.api.TrackRepository
import com.example.yp_playlist_maker.settings.data.impl.AppThemeImpl
import com.example.yp_playlist_maker.settings.domain.api.AppThemeRepository
import com.example.yp_playlist_maker.util.Constants
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {

    //Поиск треков в сети
    single<TrackRepository> {
        TrackRepositoryImpl(
            networkClient = get()
        )
    }

    //История поиска треков
    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(
            sharedPreferences = get(named(Constants.TRACK_LIST_KEY)),
            gson = get()
        )
    }

    //Тема приложения
    single<AppThemeRepository> {
        AppThemeImpl(
            sharedPreferences = get(named(Constants.THEME_PREFERENCES))
        )
    }

    //Аудиоплеер
    factory<PlayTrackRepository> {
        PlayTrackRepositoryImpl(
            mediaPlayer = get()
        )
    }

}