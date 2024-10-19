package com.example.yp_playlist_maker.player.ui.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.yp_playlist_maker.player.domain.api.PlayTrackInteractor

class AudioPlayerViewModel(
    private val playTrack: PlayTrackInteractor
) : ViewModel() {

    // Доступность кнопки
    private val playButtonEnabled = MutableLiveData<Boolean>()
    fun getPlayButtonEnabled(): LiveData<Boolean> = playButtonEnabled


    // Прозрачность кнопки
    private val playButtonAlpha = MutableLiveData<Float>()
    fun getPlayButtonAlpha(): LiveData<Float> = playButtonAlpha

    // Меняем кнопку на play/pause
    private val isPlaying = MutableLiveData<Boolean>()
    fun getIsPlaying(): LiveData<Boolean> = isPlaying

    // Меняем время воспроизведения
    private val currentTime = MutableLiveData<String>()
    fun getCurrentTime(): LiveData<String> = currentTime

    fun preparePlayer(url: String) {
        playTrack.preparePlayer(
            url,
            onPrepare = {
                playButtonEnabled.value = true
                playButtonAlpha.value = ALPHA_100
            },
            onComplete = {
                isPlaying.value = false
                currentTime.value = DEFAULT_TIME
            },
            onTimeUpdate = { time -> currentTime.value = time }
        )
    }

    fun playbackControl() {
        playTrack.playbackControl(
            onPause = { isPlaying.value = false },
            onStart = { isPlaying.value = true },
            onTimeUpdate = { time -> currentTime.value = time }
        )
    }

    fun pausePlayer() {
        playTrack.pausePlayer(
            onPause = { isPlaying.value = false }
        )
    }

    private fun releasePlayer() {
        playTrack.releasePlayer()
    }

    private fun threadRemoveCallbacks() {
        playTrack.threadRemoveCallbacks(
            onTimeUpdate = { time -> currentTime.value = time }
        )
    }

    override fun onCleared() {
        releasePlayer()
        Log.d("onCleared", "Player released")
        threadRemoveCallbacks()
        Log.d("onCleared", "Callbacks removed")
    }

    companion object {
        private const val DEFAULT_TIME = "00:00"
        private const val ALPHA_100 = 1F
    }
}