package com.example.yp_playlist_maker.data.impl

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.domain.api.repository.PlayTrackRepository
import com.example.yp_playlist_maker.domain.models.PlayerParams
import java.text.SimpleDateFormat
import java.util.Locale

class PlayTrackRepositoryImpl(private val playerParams: PlayerParams): PlayTrackRepository {

    private var playerState = STATE_DEFAULT
    private var mediaPlayer = MediaPlayer()
    private var mainThreadHandler: Handler = Handler(Looper.getMainLooper())
    private var trackTime: String = ""
    private var runnable = Runnable {
        trackTime = SimpleDateFormat("mm:ss", Locale.getDefault())
            .format(mediaPlayer.currentPosition)
            .toString()
        playerParams.timer.text = trackTime
        threadPostDelayed()
    }

    override fun preparePlayer(url: String, onPrepare: () -> Unit, onComplete: () -> Unit) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            onPrepare.invoke()
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            onComplete.invoke()
            playerState = STATE_PREPARED
            threadRemoveCallbacks()
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
        playerParams.play.setBackgroundResource(R.drawable.btn_pause)
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
        playerParams.play.setBackgroundResource(R.drawable.btn_play)
    }

    override fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()
                threadRemoveCallbacks()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
                threadPost()
            }
        }
    }

    override fun releasePlayer() {
        mediaPlayer.release()
    }

    override fun threadRemoveCallbacks() {
        mainThreadHandler.removeCallbacks(runnable)
    }

    override fun threadPostDelayed() {
        mainThreadHandler.postDelayed(runnable, MILLIS_500
        )
    }

    override fun threadPost() {
        mainThreadHandler.post(runnable)
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val MILLIS_500 = 500L
    }

}