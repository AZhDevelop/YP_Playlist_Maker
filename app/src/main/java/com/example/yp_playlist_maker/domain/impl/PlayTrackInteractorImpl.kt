package com.example.yp_playlist_maker.domain.impl

import com.example.yp_playlist_maker.domain.api.interactor.PlayTrackInteractor
import com.example.yp_playlist_maker.domain.api.repository.PlayTrackRepository

class PlayTrackInteractorImpl(private val repository: PlayTrackRepository) : PlayTrackInteractor {

    override fun preparePlayer(url: String, onPrepare: () -> Unit, onComplete: () -> Unit) {
        repository.preparePlayer(url, onPrepare, onComplete)
    }

    override fun pausePlayer() {
        repository.pausePlayer()
    }

    override fun playbackControl() {
        repository.playbackControl()
    }

    override fun releasePlayer() {
        repository.releasePlayer()
    }

    override fun threadRemoveCallbacks() {
        repository.threadRemoveCallbacks()
    }

}