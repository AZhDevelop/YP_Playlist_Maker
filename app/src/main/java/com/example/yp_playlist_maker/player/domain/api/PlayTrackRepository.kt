package com.example.yp_playlist_maker.player.domain.api

interface PlayTrackRepository {

    fun preparePlayer(
        url: String,
        onPrepare: () -> Unit,
        onComplete: () -> Unit
    )

    fun startPlayer(onStart: () -> Unit)
    fun pausePlayer(onPause: () -> Unit)
    fun playbackControl(onStart: () -> Unit, onPause: () -> Unit)
    fun releasePlayer()
    fun getTrackCurrentTime(): Int

}