package com.example.yp_playlist_maker.player.ui.view_model

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yp_playlist_maker.database.domain.api.FavouriteTracksInteractor
import com.example.yp_playlist_maker.database.domain.api.PlaylistsInteractor
import com.example.yp_playlist_maker.database.domain.models.Playlist
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
    private val favouriteTracksInteractor: FavouriteTracksInteractor,
    private val playlistsInteractor: PlaylistsInteractor
) : ViewModel() {

    private var trackTime: String = EMPTY_STRING
    private var updateTrackTimeJob: Job? = null

    private val audioPlayerStatus = MutableLiveData(State.PlayerState.LOADING)
    fun getAudioPlayerStatus(): LiveData<State.PlayerState> = audioPlayerStatus

    private val currentTime = MutableLiveData<String>()
    fun getCurrentTime(): LiveData<String> = currentTime

    private val trackData = MutableLiveData<Track>()
    fun getTrackData(): LiveData<Track> = trackData

    private val _isFavourite = MutableLiveData<Boolean>()
    val isFavourite: LiveData<Boolean> get() = _isFavourite

    private val _backgroundColor = MutableLiveData<Int>()
    val backgroundColor: LiveData<Int> get() = _backgroundColor

    fun setBackgroundColor(color: Int) {
        _backgroundColor.value = color
    }

    private val playlistList = MutableLiveData<List<Playlist>>()
    fun getPlaylistList(): LiveData<List<Playlist>> = playlistList

    private val bottomSheetState = MutableLiveData<State.BottomSheetState>()
    fun getBottomSheetState(): LiveData<State.BottomSheetState> = bottomSheetState

    fun setTrackData(track: Track) {
        viewModelScope.launch {
            _isFavourite.value = favouriteTracksInteractor.checkIsTrackFavourite(track.trackId)
            _isFavourite.value?.let { isFav ->
                trackData.value = track.copy(
                    trackId = track.trackId,
                    trackName = track.trackName,
                    artistName = track.artistName,
                    trackTimeMillis = Converter.convertMillis(track.trackTimeMillis),
                    artworkUrl100 = Converter.convertUrl(track.artworkUrl100),
                    releaseDate = track.releaseDate
                        .replaceAfter(DASH, EMPTY_STRING)
                        .replace(DASH, EMPTY_STRING),
                    primaryGenreName = track.primaryGenreName,
                    country = track.country,
                    previewUrl = track.previewUrl,
                    isFavourite = isFav
                )
                preparePlayer()
            }
        }
    }

    fun saveTrackToFavourites() {
        viewModelScope.launch {
            trackData.value?.let {
                it.isFavourite = true
                favouriteTracksInteractor.insertTrack(it)
            }
            _isFavourite.value = true
        }
    }

    fun deleteTrackFromFavourites() {
        viewModelScope.launch {
            trackData.value?.let {
                it.isFavourite = false
                favouriteTracksInteractor.deleteTrack(it)
            }
            _isFavourite.value = false
        }
    }

    fun saveTrackToPlaylist(playlistName: String) {
        viewModelScope.launch {
            trackData.value?.let {
                it.playlistName = playlistName
                favouriteTracksInteractor.insertTrack(it)
            }
        }
    }

    init {
        _backgroundColor.value = Color.TRANSPARENT
        currentTime.value = DEFAULT_TIME
        checkPlaylistList()
    }

    private fun preparePlayer() {
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

    fun checkPlaylistList() {
        viewModelScope.launch {
            playlistsInteractor
                .getPlaylistList()
                .collect{ plList ->
                    if (plList.isEmpty()) {
                        bottomSheetState.value = State.BottomSheetState.EMPTY
                        playlistList.value = plList
                    } else {
                        bottomSheetState.value = State.BottomSheetState.SUCCESS
                        playlistList.value = plList
                    }
                }
        }
    }

    override fun onCleared() {
        playTrackService.releasePlayer()
        viewModelScope.cancel()
    }

    companion object {
        private const val DEFAULT_TIME: String = "00:00"
        private const val EMPTY_STRING: String = ""
        private const val DASH: String = "-"
        private const val MILLIS_300: Long = 300L
    }
}