package com.example.yp_playlist_maker.domain.api.repository

interface PlayTrackRepository {

    fun preparePlayer(url: String, onPrepare: () -> Unit, onComplete: () -> Unit)
    fun startPlayer()
    fun pausePlayer()
    fun playbackControl()
    fun releasePlayer()
    fun threadRemoveCallbacks()
    fun threadPostDelayed()
    fun threadPost()

}