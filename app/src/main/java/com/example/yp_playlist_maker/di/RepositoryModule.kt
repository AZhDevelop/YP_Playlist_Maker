package com.example.yp_playlist_maker.di

import com.example.yp_playlist_maker.search.data.impl.SearchHistoryRepositoryImpl
import com.example.yp_playlist_maker.search.data.impl.TrackRepositoryImpl
import com.example.yp_playlist_maker.search.domain.api.SearchHistoryRepository
import com.example.yp_playlist_maker.search.domain.api.TrackRepository
import com.example.yp_playlist_maker.settings.data.impl.AppThemeImpl
import com.example.yp_playlist_maker.settings.domain.api.AppThemeRepository
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
            sharedPreferences = get(named("trackHistoryRepository"))
        )
    }

    //Тема приложения
    single<AppThemeRepository> {
        AppThemeImpl(
            sharedPreferences = get(named("appThemeRepository"))
        )
    }

}