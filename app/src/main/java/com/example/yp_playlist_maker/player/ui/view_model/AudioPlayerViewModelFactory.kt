package com.example.yp_playlist_maker.player.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yp_playlist_maker.creator.Creator

class AudioPlayerViewModelFactory : ViewModelProvider.Factory {

    private val playTrackService = Creator.providePlayTrackInteractor()

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AudioPlayerViewModel(playTrackService) as T
    }
}