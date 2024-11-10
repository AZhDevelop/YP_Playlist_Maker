package com.example.yp_playlist_maker.player.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.yp_playlist_maker.player.domain.api.PlayTrackInteractor
import com.example.yp_playlist_maker.search.domain.models.Track
import com.example.yp_playlist_maker.util.Converter
import com.example.yp_playlist_maker.util.State

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

    private val audioPlayerStatus = MutableLiveData(State.PlayerState.LOADING)
    fun getAudioPlayerStatus(): LiveData<State.PlayerState> = audioPlayerStatus

    private val currentTime = MutableLiveData<String>()
    fun getCurrentTime(): LiveData<String> = currentTime

    fun preparePlayer() {
        trackData.value?.let {
            playTrackService.preparePlayer(
                it.previewUrl,
                onPrepare = { audioPlayerStatus.value = State.PlayerState.PREPARED },
                onComplete = { audioPlayerStatus.value = State.PlayerState.COMPLETED },
                onTimeUpdate = { time -> currentTime.value = time }
            )
        }
    }

    fun playbackControl() {
        playTrackService.playbackControl(
            onStart = { audioPlayerStatus.value = State.PlayerState.START },
            onTimeUpdate = { time -> currentTime.value = time },
            onPause = { audioPlayerStatus.value = State.PlayerState.PAUSE }
        )
    }

    fun pausePlayer() {
        playTrackService.pausePlayer(
            onPause = { audioPlayerStatus.value = State.PlayerState.PAUSE }
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