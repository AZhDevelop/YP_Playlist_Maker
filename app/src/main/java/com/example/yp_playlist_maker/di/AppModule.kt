package com.example.yp_playlist_maker.di

import com.example.yp_playlist_maker.media.ui.view_model.MediaFavouritesFragmentViewModel
import com.example.yp_playlist_maker.media.ui.view_model.MediaPlaylistsFragmentViewModel
import com.example.yp_playlist_maker.player.ui.view_model.AudioPlayerViewModel
import com.example.yp_playlist_maker.playlist.ui.view_model.PlaylistFragmentViewModel
import com.example.yp_playlist_maker.playlist_editor.ui.view_model.PlaylistEditorViewModel
import com.example.yp_playlist_maker.search.ui.view_model.SearchViewModel
import com.example.yp_playlist_maker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
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
            playTrackService = get(),
            favouriteTracksInteractor = get(),
            playlistsInteractor = get(),
            tracksInPlaylistsInteractor = get()
        )
    }

    viewModel<MediaFavouritesFragmentViewModel> {
        MediaFavouritesFragmentViewModel(
            favouriteTracksInteractor = get()
        )
    }

    viewModel<MediaPlaylistsFragmentViewModel> {
        MediaPlaylistsFragmentViewModel(
            playlistsInteractor = get()
        )
    }

    viewModel<PlaylistEditorViewModel> {
        PlaylistEditorViewModel(
            playlistsInteractor = get(),
            playlistsDirectory = get(),
            contentResolver = get()
        )
    }

    viewModel<PlaylistFragmentViewModel> {
        PlaylistFragmentViewModel()
    }

}