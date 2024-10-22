package com.example.yp_playlist_maker.player.ui.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.yp_playlist_maker.player.domain.api.PlayTrackInteractor

class AudioPlayerViewModel(
    private val playTrackService: PlayTrackInteractor
) : ViewModel() {

    private val audioPlayerStatus = MutableLiveData(LOADING)
    fun getAudioPlayerStatus(): LiveData<String> = audioPlayerStatus

    private val currentTime = MutableLiveData<String>()
    fun getCurrentTime(): LiveData<String> = currentTime

    fun preparePlayer(url: String) {
        playTrackService.preparePlayer(
            url,
            onPrepare = { audioPlayerStatus.value = PREPARED },
            onComplete = { audioPlayerStatus.value = COMPLETED },
            onTimeUpdate = { time -> currentTime.value = time }
        )
    }

    fun playbackControl() {
        playTrackService.playbackControl(
            onStart = { audioPlayerStatus.value = START },
            onTimeUpdate = { time -> currentTime.value = time },
            onPause = { audioPlayerStatus.value = PAUSE }
        )
    }

    fun pausePlayer() {
        playTrackService.pausePlayer(
            onPause = { audioPlayerStatus.value = PAUSE }
        )
    }

    override fun onCleared() {
        playTrackService.releasePlayer()
        playTrackService.threadRemoveCallbacks(
            onTimeUpdate = { time -> currentTime.value = time }
        )
    }

    companion object {
        private const val LOADING = "Loading"
        private const val PREPARED = "Prepared"
        private const val COMPLETED = "Completed"
        private const val START = "Start"
        private const val PAUSE = "Pause"
    }
}