package com.example.yp_playlist_maker.player.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.yp_playlist_maker.player.domain.api.PlayTrackInteractor
import com.example.yp_playlist_maker.search.domain.models.Track
import com.example.yp_playlist_maker.util.Constants
import com.example.yp_playlist_maker.util.Converter

class AudioPlayerViewModel(
    private val playTrackService: PlayTrackInteractor,
    private val trackExtra: Track?
) : ViewModel() {

    fun getRoundedCorners(playerImageRadius: Int) : Int {
        return Converter.dpToPx(playerImageRadius)
    }

    private val trackData = MutableLiveData<Track>()
    fun getTrackData(): LiveData<Track> = trackData

    private fun setTrackData() {
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

    init {
        setTrackData()
    }

    private val audioPlayerStatus = MutableLiveData(Constants.PlayerState.LOADING)
    fun getAudioPlayerStatus(): LiveData<Constants.PlayerState> = audioPlayerStatus

    private val currentTime = MutableLiveData<String>()
    fun getCurrentTime(): LiveData<String> = currentTime

    fun preparePlayer() {
        trackData.value?.let {
            playTrackService.preparePlayer(
                it.previewUrl,
                onPrepare = { audioPlayerStatus.value = Constants.PlayerState.PREPARED },
                onComplete = { audioPlayerStatus.value = Constants.PlayerState.COMPLETED },
                onTimeUpdate = { time -> currentTime.value = time }
            )
        }
    }

    fun playbackControl() {
        playTrackService.playbackControl(
            onStart = { audioPlayerStatus.value = Constants.PlayerState.START },
            onTimeUpdate = { time -> currentTime.value = time },
            onPause = { audioPlayerStatus.value = Constants.PlayerState.PAUSE }
        )
    }

    fun pausePlayer() {
        playTrackService.pausePlayer(
            onPause = { audioPlayerStatus.value = Constants.PlayerState.PAUSE }
        )
    }

    override fun onCleared() {
        playTrackService.releasePlayer()
        playTrackService.threadRemoveCallbacks(
            onTimeUpdate = { time -> currentTime.value = time }
        )
    }

    companion object {
        private const val EMPTY_STRING: String = ""
        const val DASH = "-"
    }
}