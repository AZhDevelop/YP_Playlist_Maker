package com.example.yp_playlist_maker.data.impl

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.example.yp_playlist_maker.domain.api.repository.PlayTrackRepository
import java.text.SimpleDateFormat
import java.util.Locale

class PlayTrackRepositoryImpl: PlayTrackRepository {

    private var playerState = STATE_DEFAULT
    private var mediaPlayer = MediaPlayer()
    private var mainThreadHandler: Handler = Handler(Looper.getMainLooper())
    private var trackTime: String = ""
    private var receiveCallbacks = true

    private fun getRunnable(onTrackUpdate: (String) -> Unit) : Runnable {
        val runnable = Runnable {
            if (receiveCallbacks) {
                trackTime = SimpleDateFormat("mm:ss", Locale.getDefault())
                    .format(mediaPlayer.currentPosition)
                    .toString()
                onTrackUpdate.invoke(trackTime)
                threadPostDelayed(onTrackUpdate)
            }
        }
        return runnable
    }

    override fun preparePlayer(url: String, onPrepare: () -> Unit, onComplete: () -> Unit, onTrackUpdate: (String) -> Unit) {
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

    override fun playbackControl(onStart: () -> Unit, onPause: () -> Unit, onTrackUpdate: (String) -> Unit) {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer(onPause)
                threadRemoveCallbacks(onTrackUpdate)
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer(onStart)
                threadPost(onTrackUpdate)
            }
        }
    }

    override fun releasePlayer() {
        mediaPlayer.release()
    }

    override fun threadRemoveCallbacks(onTrackUpdate: (String) -> Unit) {
        mainThreadHandler.removeCallbacks(getRunnable(onTrackUpdate))
        receiveCallbacks = false
    }

    override fun threadPostDelayed(onTrackUpdate: (String) -> Unit) {
        mainThreadHandler.postDelayed(getRunnable(onTrackUpdate), MILLIS_500)
    }

    override fun threadPost(onTrackUpdate: (String) -> Unit) {
        mainThreadHandler.post(getRunnable(onTrackUpdate))
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val MILLIS_500 = 500L
    }

}