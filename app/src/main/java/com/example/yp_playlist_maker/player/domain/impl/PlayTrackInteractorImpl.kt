package com.example.yp_playlist_maker.player.domain.impl

import com.example.yp_playlist_maker.player.domain.api.PlayTrackInteractor
import com.example.yp_playlist_maker.player.domain.api.PlayTrackRepository
import com.example.yp_playlist_maker.util.State

class PlayTrackInteractorImpl(private val repository: PlayTrackRepository) : PlayTrackInteractor {

    override fun preparePlayer(
        url: String,
        onPrepare: () -> Unit,
        onComplete: () -> Unit,
        onTimeUpdate: (String) -> Unit
    ) {
        repository.preparePlayer(url, onPrepare, onComplete, onTimeUpdate)
    }

    override fun pausePlayer(onPause: () -> Unit) {
        repository.pausePlayer(onPause)
    }

    override fun playbackControl(
        onStart: () -> Unit,
        onPause: () -> Unit,
        onTimeUpdate: (String) -> Unit
    ) {
        repository.playbackControl(onStart, onPause, onTimeUpdate)
        State.SearchState.SEARCH_ERROR
    }

    override fun releasePlayer() {
        repository.releasePlayer()
    }

    override fun threadRemoveCallbacks(onTimeUpdate: (String) -> Unit) {
        repository.threadRemoveCallbacks(onTimeUpdate)
    }

}