package com.example.yp_playlist_maker.domain.use_case

import com.example.yp_playlist_maker.domain.api.interactor.PlayTrackInteractor

class PlayTrackUseCase(private val interactor: PlayTrackInteractor) {

    fun pause() {
        interactor.pausePlayer()
    }

    fun preparePlayer() {
        interactor.preparePlayer()
    }

    fun playBackControl() {
        interactor.playbackControl()
    }

    fun releasePlayer() {
        interactor.releasePlayer()
    }

    fun threadRemoveCallbacks() {
        interactor.threadRemoveCallbacks()
    }

}