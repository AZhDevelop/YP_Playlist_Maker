package com.example.yp_playlist_maker.domain.api.repository

interface PlayTrackRepository {

    fun preparePlayer(url: String, onPrepare: () -> Unit, onComplete: () -> Unit)
    fun startPlayer(onStart: () -> Unit)
    fun pausePlayer()
    fun playbackControl(onStart: () -> Unit)
    fun releasePlayer()
    fun threadRemoveCallbacks()
    fun threadPostDelayed()
    fun threadPost()

}