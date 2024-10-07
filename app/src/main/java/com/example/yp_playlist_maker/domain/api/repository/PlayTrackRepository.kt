package com.example.yp_playlist_maker.domain.api.repository

interface PlayTrackRepository {

    fun preparePlayer(
        url: String,
        onPrepare: () -> Unit,
        onComplete: () -> Unit,
        onTrackUpdate: (String) -> Unit
    )

    fun startPlayer(onStart: () -> Unit)
    fun pausePlayer(onPause: () -> Unit)
    fun playbackControl(onStart: () -> Unit, onPause: () -> Unit, onTrackUpdate: (String) -> Unit)
    fun releasePlayer()
    fun threadRemoveCallbacks(onTrackUpdate: (String) -> Unit)
    fun threadPostDelayed(onTrackUpdate: (String) -> Unit)
    fun threadPost(onTrackUpdate: (String) -> Unit)

}