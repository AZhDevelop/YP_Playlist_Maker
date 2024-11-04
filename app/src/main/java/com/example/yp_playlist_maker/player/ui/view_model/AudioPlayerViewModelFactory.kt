package com.example.yp_playlist_maker.player.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yp_playlist_maker.creator.Creator
import com.example.yp_playlist_maker.search.domain.models.Track

class AudioPlayerViewModelFactory(private val trackExtra: Track?) : ViewModelProvider.Factory {

    private val playTrackService = Creator.providePlayTrackInteractor()

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AudioPlayerViewModel(playTrackService, trackExtra) as T
    }
}