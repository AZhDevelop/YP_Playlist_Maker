package com.example.yp_playlist_maker.di

import com.example.yp_playlist_maker.database.data.FavouriteTracksRepositoryImpl
import com.example.yp_playlist_maker.database.data.converters.TrackDbConverter
import com.example.yp_playlist_maker.database.domain.FavouriteTracksRepository
import com.example.yp_playlist_maker.player.data.impl.PlayTrackRepositoryImpl
import com.example.yp_playlist_maker.player.domain.api.PlayTrackRepository
import com.example.yp_playlist_maker.search.data.impl.SearchHistoryRepositoryImpl
import com.example.yp_playlist_maker.search.data.impl.TrackRepositoryImpl
import com.example.yp_playlist_maker.search.domain.api.SearchHistoryRepository
import com.example.yp_playlist_maker.search.domain.api.TrackRepository
import com.example.yp_playlist_maker.settings.data.impl.AppThemeImpl
import com.example.yp_playlist_maker.settings.domain.api.AppThemeRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val TRACK_LIST_KEY: String = "track_list_key"
private const val THEME_PREFERENCES: String = "theme_preferences"

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
            sharedPreferences = get(named(TRACK_LIST_KEY)),
            gson = get()
        )
    }

    //Тема приложения
    single<AppThemeRepository> {
        AppThemeImpl(
            sharedPreferences = get(named(THEME_PREFERENCES))
        )
    }

    //Аудиоплеер
    factory<PlayTrackRepository> {
        PlayTrackRepositoryImpl(
            mediaPlayer = get()
        )
    }

    factory<TrackDbConverter> { TrackDbConverter() }

    single<FavouriteTracksRepository> {
        FavouriteTracksRepositoryImpl(
            appDatabase = get(),
            trackDbConverter = get()
        )
    }

}