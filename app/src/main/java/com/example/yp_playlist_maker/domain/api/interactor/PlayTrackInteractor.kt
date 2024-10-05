package com.example.yp_playlist_maker.domain.api.interactor

interface PlayTrackInteractor {

    fun preparePlayer()
    fun pausePlayer()
    fun playbackControl()
    fun releasePlayer()
    fun threadRemoveCallbacks()

}