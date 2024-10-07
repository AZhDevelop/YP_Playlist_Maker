package com.example.yp_playlist_maker.domain.api.interactor

interface PlayTrackInteractor {

    fun preparePlayer(url: String, onPrepare: () -> Unit, onComplete: () -> Unit)
    fun pausePlayer()
    fun playbackControl(onStart: () -> Unit)
    fun releasePlayer()
    fun threadRemoveCallbacks()

}