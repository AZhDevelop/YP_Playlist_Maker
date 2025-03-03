package com.example.yp_playlist_maker.player.ui.view_model

import android.graphics.Color
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yp_playlist_maker.database.domain.api.FavouriteTracksInteractor
import com.example.yp_playlist_maker.database.domain.api.PlaylistsInteractor
import com.example.yp_playlist_maker.database.domain.api.TracksInPlaylistsInteractor
import com.example.yp_playlist_maker.database.domain.models.Playlist
import com.example.yp_playlist_maker.database.domain.models.TracksInPlaylists
import com.example.yp_playlist_maker.player.domain.api.PlayTrackInteractor
import com.example.yp_playlist_maker.search.domain.models.Track
import com.example.yp_playlist_maker.util.Converter
import com.example.yp_playlist_maker.util.State
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerViewModel(
    private val playTrackService: PlayTrackInteractor,
    private val favouriteTracksInteractor: FavouriteTracksInteractor,
    private val playlistsInteractor: PlaylistsInteractor,
    private val tracksInPlaylistsInteractor: TracksInPlaylistsInteractor,
    private val converter: Converter
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

    private val _bottomSheetStateValue = MutableLiveData<Int>()
    val bottomSheetStateValue: LiveData<Int> get() = _bottomSheetStateValue

    private val addToPlaylistState = MutableLiveData<State.AddToPlaylistState>()
    fun getAddToPlaylistState(): LiveData<State.AddToPlaylistState> = addToPlaylistState

    fun setBottomSheetStateValue(state: Int) {
        if (state == BottomSheetBehavior.STATE_HIDDEN || state == BottomSheetBehavior.STATE_EXPANDED) {
            _bottomSheetStateValue.value = state
        }
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
                    trackTimeMillis = track.trackTimeMillis,
                    artworkUrl100 = converter.convertUrl(track.artworkUrl100),
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

    private fun setTrackToPlaylist(playlist: Playlist, track: Track): TracksInPlaylists {
        return TracksInPlaylists(
            elementId = 0,
            playlistId = playlist.playlistId,
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl
        )
    }

    fun saveTrackToFavourites() {
        viewModelScope.launch {
            trackData.value?.let {
                val track = Track(
                    trackId = it.trackId,
                    trackName = it.trackName,
                    artistName = it.artistName,
                    trackTimeMillis = it.trackTimeMillis,
                    artworkUrl100 = it.artworkUrl100,
                    collectionName = it.collectionName,
                    releaseDate = it.releaseDate,
                    primaryGenreName = it.primaryGenreName,
                    country = it.country,
                    previewUrl = it.previewUrl,
                    isFavourite = true
                )
                favouriteTracksInteractor.insertTrack(track)
            }
            _isFavourite.value = true
        }
    }

    fun deleteTrackFromFavourites() {
        viewModelScope.launch {
            trackData.value?.let {
                val track = Track(
                    trackId = it.trackId,
                    trackName = it.trackName,
                    artistName = it.artistName,
                    trackTimeMillis = it.trackTimeMillis,
                    artworkUrl100 = it.artworkUrl100,
                    collectionName = it.collectionName,
                    releaseDate = it.releaseDate,
                    primaryGenreName = it.primaryGenreName,
                    country = it.country,
                    previewUrl = it.previewUrl,
                    isFavourite = false
                )
                favouriteTracksInteractor.deleteTrack(track)
            }
            _isFavourite.value = false
        }
    }

    init {
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
        return converter.dpToPx(playerImageRadius)
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

    fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        viewModelScope.launch {
            var trackInPlaylists: List<Int> = listOf()
            tracksInPlaylistsInteractor
                .checkTrackInPlaylist(track.trackId)
                .collect { playlistsId ->
                    trackInPlaylists = playlistsId
                }
            if (playlist.playlistId in trackInPlaylists) {
                addToPlaylistState.value = State.AddToPlaylistState.ERROR
            } else {
                val trackToPlaylist = setTrackToPlaylist(playlist, track)
                var playlistSize = playlistsInteractor.getPlaylistSize(playlist.playlistId).toInt()
                var playlistDuration = playlistsInteractor.getPlaylistDuration(playlist.playlistId).toInt()
                tracksInPlaylistsInteractor.insertTrackToPlaylist(trackToPlaylist)
                playlistSize += 1
                playlistDuration += track.trackTimeMillis.toInt()
                playlistsInteractor.updatePlaylist(
                    Playlist(
                        playlistId = playlist.playlistId,
                        playlistName = playlist.playlistName,
                        playlistDescription = playlist.playlistDescription,
                        playlistCoverPath = playlist.playlistCoverPath,
                        playlistSize = playlistSize.toString(),
                        playlistDuration = playlistDuration.toString()
                    )
                )
                addToPlaylistState.value = State.AddToPlaylistState.SUCCESS
            }
        }
    }

    fun convertTime(time: String): String {
        return converter.convertMillis(time)
    }

    override fun onCleared() {
        playTrackService.releasePlayer()
        viewModelScope.cancel()
    }

    private companion object {
        const val DEFAULT_TIME: String = "00:00"
        const val EMPTY_STRING: String = ""
        const val DASH: String = "-"
        const val MILLIS_300: Long = 300L
    }
}