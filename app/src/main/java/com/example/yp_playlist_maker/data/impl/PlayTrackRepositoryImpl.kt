package com.example.yp_playlist_maker.data.impl

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.example.yp_playlist_maker.domain.api.repository.PlayTrackRepository
import java.text.SimpleDateFormat
import java.util.Locale

class PlayTrackRepositoryImpl : PlayTrackRepository {

    private var playerState = STATE_DEFAULT
    private var mediaPlayer = MediaPlayer()
    private var mainThreadHandler: Handler = Handler(Looper.getMainLooper())
    private var trackTime: String = ""
    private var receiveCallbacks = true

    private fun getRunnable(onTimeUpdate: (String) -> Unit): Runnable {
        val runnable = Runnable {
            if (receiveCallbacks) {
                trackTime = SimpleDateFormat("mm:ss", Locale.getDefault())
                    .format(mediaPlayer.currentPosition)
                    .toString()
                onTimeUpdate.invoke(trackTime)
                threadPostDelayed(onTimeUpdate)
            }
        }
        return runnable
    }

    override fun preparePlayer(
        url: String,
        onPrepare: () -> Unit,
        onComplete: () -> Unit,
        onTrackUpdate: (String) -> Unit
    ) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            onPrepare.invoke()
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            onComplete.invoke()
            playerState = STATE_PREPARED
            threadRemoveCallbacks(onTrackUpdate)
        }
    }

    override fun startPlayer(onStart: () -> Unit) {
        receiveCallbacks = true
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
        onPause: () -> Unit,
        onTimeUpdate: (String) -> Unit
    ) {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer(onPause)
                threadRemoveCallbacks(onTimeUpdate)
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer(onStart)
                threadPost(onTimeUpdate)
            }
        }
    }

    override fun releasePlayer() {
        mediaPlayer.release()
    }

    override fun threadRemoveCallbacks(onTimeUpdate: (String) -> Unit) {
        mainThreadHandler.removeCallbacks(getRunnable(onTimeUpdate))
        receiveCallbacks = false
    }

    override fun threadPostDelayed(onTimeUpdate: (String) -> Unit) {
        mainThreadHandler.postDelayed(getRunnable(onTimeUpdate), MILLIS_500)
    }

    override fun threadPost(onTimeUpdate: (String) -> Unit) {
        mainThreadHandler.post(getRunnable(onTimeUpdate))
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val MILLIS_500 = 500L
    }

}