package com.example.yp_playlist_maker.domain.api.repository

interface PlayTrackRepository {

    fun preparePlayer(
        url: String,
        onPrepare: () -> Unit,
        onComplete: () -> Unit,
        onTimeUpdate: (String) -> Unit
    )

    fun startPlayer(onStart: () -> Unit)
    fun pausePlayer(onPause: () -> Unit)
    fun playbackControl(onStart: () -> Unit, onPause: () -> Unit, onTimeUpdate: (String) -> Unit)
    fun releasePlayer()
    fun threadRemoveCallbacks(onTimeUpdate: (String) -> Unit)
    fun threadPostDelayed(onTimeUpdate: (String) -> Unit)
    fun threadPost(onTimeUpdate: (String) -> Unit)

}