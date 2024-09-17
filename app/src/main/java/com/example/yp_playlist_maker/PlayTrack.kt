package com.example.yp_playlist_maker

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.Locale

class PlayTrack(
    private val url: String,
    private val play: Button,
    private val timer: TextView)
{

    private var playerState = STATE_DEFAULT
    private var mediaPlayer = MediaPlayer()
    private var mainThreadHandler: Handler = Handler(Looper.getMainLooper())
    private var trackTime: String = ""
    private var runnable = Runnable {
        trackTime = SimpleDateFormat("mm:ss", Locale.getDefault())
            .format(mediaPlayer.currentPosition)
            .toString()
        timer.text = trackTime
        threadPostDelayed()
    }

    fun preparePlayer() {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            play.isEnabled = true
            play.alpha = ALPHA_100
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            play.setBackgroundResource(R.drawable.btn_play)
            playerState = STATE_PREPARED
            threadRemoveCallbacks()
            timer.text = DEFAULT_TIME
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
        play.setBackgroundResource(R.drawable.btn_pause)
    }

    fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
        play.setBackgroundResource(R.drawable.btn_play)
    }

    fun playbackControl() {
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

    fun releasePlayer() {
        mediaPlayer.release()
    }

    fun threadRemoveCallbacks() {
        mainThreadHandler.removeCallbacks(runnable)
    }

    private fun threadPostDelayed() {
        mainThreadHandler.postDelayed(runnable, MILLIS_500)
    }

    private fun threadPost() {
        mainThreadHandler.post(runnable)
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DEFAULT_TIME = "00:00"
        private const val MILLIS_500 = 500L
        private const val ALPHA_100 = 1F
    }

}