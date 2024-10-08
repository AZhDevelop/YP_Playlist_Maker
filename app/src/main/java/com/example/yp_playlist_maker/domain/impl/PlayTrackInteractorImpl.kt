package com.example.yp_playlist_maker.domain.impl

import com.example.yp_playlist_maker.domain.api.interactor.PlayTrackInteractor
import com.example.yp_playlist_maker.domain.api.repository.PlayTrackRepository

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
    }

    override fun releasePlayer() {
        repository.releasePlayer()
    }

    override fun threadRemoveCallbacks(onTimeUpdate: (String) -> Unit) {
        repository.threadRemoveCallbacks(onTimeUpdate)
    }

}