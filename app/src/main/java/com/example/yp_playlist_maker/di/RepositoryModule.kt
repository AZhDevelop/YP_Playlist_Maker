package com.example.yp_playlist_maker.di

import com.example.yp_playlist_maker.database.data.impl.FavouriteTracksRepositoryImpl
import com.example.yp_playlist_maker.database.data.impl.PlaylistRepositoryImpl
import com.example.yp_playlist_maker.database.domain.api.FavouriteTracksRepository
import com.example.yp_playlist_maker.database.domain.api.PlaylistsRepository
import com.example.yp_playlist_maker.player.data.impl.PlayTrackRepositoryImpl
import com.example.yp_playlist_maker.player.domain.api.PlayTrackRepository
import com.example.yp_playlist_maker.search.data.impl.SearchHistoryRepositoryImpl
import com.example.yp_playlist_maker.search.data.impl.TrackRepositoryImpl
import com.example.yp_playlist_maker.search.domain.api.SearchHistoryRepository
import com.example.yp_playlist_maker.search.domain.api.TrackRepository
import com.example.yp_playlist_maker.settings.data.impl.AppThemeImpl
import com.example.yp_playlist_maker.settings.domain.api.AppThemeRepository
import com.example.yp_playlist_maker.util.Converter
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

    factory<Converter> { Converter }

    single<FavouriteTracksRepository> {
        FavouriteTracksRepositoryImpl(
            trackDao = get(),
            converter = get()
        )
    }

    single<PlaylistsRepository> {
        PlaylistRepositoryImpl(
            playlistDao = get(),
            converter = get()
        )
    }

}