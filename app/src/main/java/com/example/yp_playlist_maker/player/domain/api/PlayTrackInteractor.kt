package com.example.yp_playlist_maker.player.domain.api

interface PlayTrackInteractor {

    fun preparePlayer(
        url: String,
        onPrepare: () -> Unit,
        onComplete: () -> Unit,
        onTimeUpdate: (String) -> Unit
    )

    fun pausePlayer(onPause: () -> Unit)
    fun playbackControl(onStart: () -> Unit, onPause: () -> Unit, onTimeUpdate: (String) -> Unit)
    fun releasePlayer()
    fun threadRemoveCallbacks(onTimeUpdate: (String) -> Unit)

}