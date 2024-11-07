package com.example.yp_playlist_maker.player.ui.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.yp_playlist_maker.player.domain.api.PlayTrackInteractor
import com.example.yp_playlist_maker.search.domain.models.Track
import com.example.yp_playlist_maker.util.Converter

class AudioPlayerViewModel(
    private val playTrackService: PlayTrackInteractor,
) : ViewModel() {

    fun getRoundedCorners(playerImageRadius: Int) : Int {
        return Converter.dpToPx(playerImageRadius)
    }

    private val trackData = MutableLiveData<Track>()
    fun getTrackData(): LiveData<Track> = trackData

    fun setTrackData(trackExtra: Track?) {
        trackData.value = trackExtra?.let {
            Track(
                trackName = it.trackName,
                artistName = trackExtra.artistName,
                trackTimeMillis = Converter.convertMillis(trackExtra.trackTimeMillis),
                artworkUrl100 = Converter.convertUrl(trackExtra.artworkUrl100),
                collectionName = trackExtra.collectionName,
                releaseDate = trackExtra.releaseDate
                    .replaceAfter(DASH, EMPTY_STRING)
                    .replace(DASH, EMPTY_STRING),
                primaryGenreName = trackExtra.primaryGenreName,
                country = trackExtra.country,
                previewUrl = trackExtra.previewUrl,
            )
        }
    }

    private val audioPlayerStatus = MutableLiveData(LOADING)
    fun getAudioPlayerStatus(): LiveData<String> = audioPlayerStatus

    private val currentTime = MutableLiveData<String>()
    fun getCurrentTime(): LiveData<String> = currentTime

    fun preparePlayer() {
        trackData.value?.let {
            playTrackService.preparePlayer(
                it.previewUrl,
                onPrepare = { audioPlayerStatus.value = PREPARED },
                onComplete = { audioPlayerStatus.value = COMPLETED },
                onTimeUpdate = { time -> currentTime.value = time }
            )
        }
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
        private const val EMPTY_STRING = ""
        private const val DASH = "-"
    }
}