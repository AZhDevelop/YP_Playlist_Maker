package com.example.yp_playlist_maker.domain.api.interactor

interface PlayTrackInteractor {

    fun preparePlayer(
        url: String,
        onPrepare: () -> Unit,
        onComplete: () -> Unit,
        onTrackUpdate: (String) -> Unit
    )

    fun pausePlayer(onPause: () -> Unit)
    fun playbackControl(onStart: () -> Unit, onPause: () -> Unit, onTrackUpdate: (String) -> Unit)
    fun releasePlayer()
    fun threadRemoveCallbacks(onTrackUpdate: (String) -> Unit)

}