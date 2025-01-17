package com.example.yp_playlist_maker.player.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yp_playlist_maker.database.domain.api.FavouriteTracksInteractor
import com.example.yp_playlist_maker.player.domain.api.PlayTrackInteractor
import com.example.yp_playlist_maker.search.domain.models.Track
import com.example.yp_playlist_maker.util.Converter
import com.example.yp_playlist_maker.util.State
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerViewModel(
    private val playTrackService: PlayTrackInteractor,
    private val trackExtra: Track?,
    private val favouriteTracksInteractor: FavouriteTracksInteractor
) : ViewModel() {

    private var trackTime: String = EMPTY_STRING
    private var updateTrackTimeJob: Job? = null

    private val audioPlayerStatus = MutableLiveData(State.PlayerState.LOADING)
    fun getAudioPlayerStatus(): LiveData<State.PlayerState> = audioPlayerStatus

    private val currentTime = MutableLiveData<String>()
    fun getCurrentTime(): LiveData<String> = currentTime

    private val trackData = MutableLiveData<Track>()
    fun getTrackData(): LiveData<Track> = trackData

    private val isFavourite = MutableLiveData(false)
    fun getIsFavourite(): LiveData<Boolean> = isFavourite

    private fun setTrackData() {
        trackData.value = trackExtra?.let {
            Track(
                trackId = trackExtra.trackId,
                trackName = trackExtra.trackName,
                artistName = trackExtra.artistName,
                trackTimeMillis = if (trackExtra.isFavourite) trackExtra.trackTimeMillis else Converter.convertMillis(trackExtra.trackTimeMillis),
                artworkUrl100 = Converter.convertUrl(trackExtra.artworkUrl100),
                collectionName = trackExtra.collectionName,
                releaseDate = trackExtra.releaseDate
                    .replaceAfter(DASH, EMPTY_STRING)
                    .replace(DASH, EMPTY_STRING),
                primaryGenreName = trackExtra.primaryGenreName,
                country = trackExtra.country,
                previewUrl = trackExtra.previewUrl,
                isFavourite = trackExtra.isFavourite
            )
        }
    }

    private fun checkIsFavouriteTrack(trackId: String) {
        viewModelScope.launch {
            isFavourite.value = favouriteTracksInteractor.checkIsTrackFavourite(trackId)
        }
    }

    fun saveTrackToFavourites() {
        viewModelScope.launch {
            trackData.value?.let {
                it.isFavourite = true
                favouriteTracksInteractor.insertTrack(it)
            }
            isFavourite.value = true
        }
    }

    fun deleteTrackFromFavourites() {
        viewModelScope.launch {
            trackData.value?.let {
                it.isFavourite = false
                favouriteTracksInteractor.deleteTrack(it)
            }
            isFavourite.value = false
        }
    }

    init {
        setTrackData()
        trackData.value?.let { checkIsFavouriteTrack(it.trackId) }
        currentTime.value = DEFAULT_TIME
    }

    fun preparePlayer() {
        trackData.value?.let {
            playTrackService.preparePlayer(
                it.previewUrl,
                onPrepare = { audioPlayerStatus.value = State.PlayerState.PREPARED },
                onComplete = {
                    audioPlayerStatus.value = State.PlayerState.COMPLETED
                    updateTrackTimeJob?.cancel()
                    currentTime.value = DEFAULT_TIME
                }
            )
        }
    }

    fun playbackControl() {
        playTrackService.playbackControl(
            onStart = {
                audioPlayerStatus.value = State.PlayerState.START
                updateTrackTime()
            },
            onPause = {
                audioPlayerStatus.value = State.PlayerState.PAUSE
                updateTrackTimeJob?.cancel()
            }
        )
    }

    fun pausePlayer() {
        playTrackService.pausePlayer(
            onPause = {
                audioPlayerStatus.value = State.PlayerState.PAUSE
                updateTrackTimeJob?.cancel()
            }
        )
    }

    override fun onCleared() {
        playTrackService.releasePlayer()
        viewModelScope.cancel()
    }

    private fun updateTrackTime() {
        updateTrackTimeJob = viewModelScope.launch {
            while (audioPlayerStatus.value == State.PlayerState.START) {
                delay(MILLIS_300)
                trackTime = SimpleDateFormat("mm:ss", Locale.getDefault())
                    .format(playTrackService.getTrackCurrentTime())
                    .toString()
                currentTime.value = trackTime
            }
        }
    }

    fun getRoundedCorners(playerImageRadius: Int): Int {
        return Converter.dpToPx(playerImageRadius)
    }

    companion object {
        private const val DEFAULT_TIME: String = "00:00"
        private const val EMPTY_STRING: String = ""
        private const val DASH: String = "-"
        private const val MILLIS_300: Long = 300L
    }
}