package com.example.yp_playlist_maker.playlist.ui.view_model

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.database.domain.api.PlaylistsInteractor
import com.example.yp_playlist_maker.database.domain.api.TracksInPlaylistsInteractor
import com.example.yp_playlist_maker.database.domain.models.Playlist
import com.example.yp_playlist_maker.database.domain.models.TracksInPlaylists
import com.example.yp_playlist_maker.search.domain.models.Track
import com.example.yp_playlist_maker.util.Converter
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class PlaylistFragmentViewModel(
    private val playlistsInteractor: PlaylistsInteractor,
    private val tracksInPlaylistsInteractor: TracksInPlaylistsInteractor,
    private val converter: Converter
): ViewModel() {

    private var updatePlaylistJob: Job? = null
    private var setSharedMessageJob: Job? = null
    private var deletePlaylistJob: Job? = null

    private val playlistData = MutableLiveData<Playlist>()
    fun getPlaylistData(): LiveData<Playlist> = playlistData

    private val tracksInPlaylist = MutableLiveData<List<Track>>()
    fun getTracksInPlaylist(): LiveData<List<Track>> = tracksInPlaylist

    private val sharedMessage = MutableLiveData<String>()
    fun getSharedMessage(): LiveData<String> = sharedMessage

    private val deletePlaylistStatus = MutableLiveData(false)
    fun getDeletePlaylistStatus(): LiveData<Boolean> = deletePlaylistStatus

    private val _backgroundColor = MutableLiveData<Int>()
    val backgroundColor: LiveData<Int> get() = _backgroundColor

    fun setBackgroundColor(color: Int) {
        _backgroundColor.value = color
    }

    fun updatePlaylistData(playlist: Playlist) {
        viewModelScope.launch {
            playlistsInteractor
                .getPlaylistData(playlist.playlistId)
                .collect { playlist ->
                    playlistData.value = Playlist(
                        playlistId = playlist[0].playlistId,
                        playlistName = playlist[0].playlistName,
                        playlistDescription = playlist[0].playlistDescription,
                        playlistCoverPath = playlist[0].playlistCoverPath,
                        playlistSize = playlist[0].playlistSize,
                        playlistDuration = playlist[0].playlistDuration
                    )
                }
        }
    }

    fun setPlaylistData(playlist: Playlist) {
        viewModelScope.launch {
            playlistData.value = Playlist(
                playlistId = playlist.playlistId,
                playlistName = playlist.playlistName,
                playlistDescription = playlist.playlistDescription,
                playlistCoverPath = playlist.playlistCoverPath,
                playlistSize = playlist.playlistSize,
                playlistDuration = playlist.playlistDuration
            )
        }
    }

    fun setTracksInPlaylist(playlistId: Int) {
        viewModelScope.launch {
            tracksInPlaylistsInteractor
                .getTracksFromPlaylist(playlistId)
                .collect { playlistTracks ->
                    val trackList = playlistTracks.map { converter.convertTracksInPlaylistToTrack(it) }
                    tracksInPlaylist.value = trackList.reversed()
                }
        }
    }

    fun deleteTrackFromPlaylist(track: Track, playlist: Playlist) {
        updatePlaylistJob = viewModelScope.launch {
            val elementId = tracksInPlaylistsInteractor.getElementId(playlist.playlistId, track.trackId)
            val trackToPlaylist = setTrackToPlaylist(playlist, track, elementId)
            var playlistSize = playlistsInteractor.getPlaylistSize(playlist.playlistId).toInt()
            var playlistDuration = playlistsInteractor.getPlaylistDuration(playlist.playlistId).toInt()
            tracksInPlaylistsInteractor.deleteTrackFromPlaylist(trackToPlaylist)
            playlistSize -= 1
            playlistDuration -= track.trackTimeMillis.toInt()
            val newPlaylistData = Playlist(
                playlistId = playlist.playlistId,
                playlistName = playlist.playlistName,
                playlistDescription = playlist.playlistDescription,
                playlistCoverPath = playlist.playlistCoverPath,
                playlistSize = playlistSize.toString(),
                playlistDuration = playlistDuration.toString()
            )
            playlistsInteractor.updatePlaylist(newPlaylistData)
            setPlaylistData(newPlaylistData)
            setTracksInPlaylist(newPlaylistData.playlistId)
            setTrackListMessage(newPlaylistData)
        }
    }

    private fun setTrackToPlaylist(playlist: Playlist, track: Track, elementId: Int): TracksInPlaylists {
        return TracksInPlaylists(
            elementId = elementId,
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

    fun setTrackListMessage(playlist: Playlist) {
        if (playlist.playlistSize == "0") {
            sharedMessage.value = "0"
        } else {
            val playlistName = "Название плейлиста: ${playlist.playlistName}\n"
            val playlistDescription = "Описание: ${playlist.playlistDescription.ifEmpty { "Нет описания" }}\n"
            val playlistSize = "Количество треков: ${convertTime(playlist.playlistSize)}\n"
            var message = playlistName + playlistDescription + playlistSize
            var counter = 1
            setSharedMessageJob = viewModelScope.launch {
                tracksInPlaylistsInteractor
                    .getTracksFromPlaylist(playlist.playlistId)
                    .collect { playlistTracks ->
                        for (track in playlistTracks) {
                            message += "$counter. ${track.artistName} - ${track.trackName} " +
                                    "(${convertTime(track.trackTimeMillis)})\n"
                            counter++
                        }
                    }
                sharedMessage.value = message
            }
        }
    }

    fun deletePlaylist(playlist: Playlist) {
        deletePlaylistJob = viewModelScope.launch {
            tracksInPlaylistsInteractor
                .getTracksFromPlaylist(playlist.playlistId)
                .collect { playlistTracks ->
                    for (track in playlistTracks) {
                        tracksInPlaylistsInteractor.deleteTrackFromPlaylist(track)
                    }
                }
            playlistsInteractor.deletePlaylist(playlist)
            deletePlaylistStatus.value = true
        }
    }

    fun sharePlaylistData(context: Context, message: String) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, message)
        }

        val createChooser = Intent.createChooser(shareIntent, context.getString(R.string.share_with))
        createChooser.addFlags(FLAG_ACTIVITY_NEW_TASK)

        context.startActivity(createChooser)
    }

    fun convertTime(time: String): String {
        return converter.convertMillis(time)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}