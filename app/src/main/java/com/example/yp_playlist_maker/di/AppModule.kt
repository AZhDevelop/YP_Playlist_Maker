package com.example.yp_playlist_maker.di

import com.example.yp_playlist_maker.player.ui.view_model.AudioPlayerViewModel
import com.example.yp_playlist_maker.search.ui.view_model.SearchViewModel
import com.example.yp_playlist_maker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {

    viewModel<SearchViewModel> {
        SearchViewModel(
            searchTrackService = get(),
            searchHistoryService = get()
        )
    }

    viewModel<SettingsViewModel> {
        SettingsViewModel(
            appTheme = get()
        )
    }

    viewModel<AudioPlayerViewModel> {
        AudioPlayerViewModel(
            playTrackService = get()
        )
    }

}