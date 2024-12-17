package com.example.yp_playlist_maker.player.data.impl

import android.media.MediaPlayer
import android.util.Log
import com.example.yp_playlist_maker.player.domain.api.PlayTrackRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayTrackRepositoryImpl(private val mediaPlayer: MediaPlayer): PlayTrackRepository {

    private var playerState = STATE_DEFAULT

    override fun preparePlayer(
        url: String,
        onPrepare: () -> Unit,
        onComplete: () -> Unit
    ) {
        mediaPlayer.apply {
            reset()
            setDataSource(url)
            prepareAsync()
            setOnPreparedListener {
                onPrepare.invoke()
                playerState = STATE_PREPARED
            }
            setOnCompletionListener {
                onComplete.invoke()
                playerState = STATE_PREPARED
            }
        }
    }

    override fun startPlayer(onStart: () -> Unit) {
        mediaPlayer.start()
        playerState = STATE_PLAYING
        onStart.invoke()
    }

    override fun pausePlayer(onPause: () -> Unit) {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
        onPause.invoke()
    }

    override fun playbackControl(
        onStart: () -> Unit,
        onPause: () -> Unit
    ) {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer(onPause)
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer(onStart)
            }
        }
    }

    override fun releasePlayer() {
        mediaPlayer.release()
    }

    override fun getTrackCurrentTime(): Int {
        return mediaPlayer.currentPosition
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }

}