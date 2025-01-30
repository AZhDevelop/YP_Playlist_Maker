package com.example.yp_playlist_maker.playlist.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yp_playlist_maker.database.domain.api.PlaylistsInteractor
import com.example.yp_playlist_maker.database.domain.api.TracksInPlaylistsInteractor
import com.example.yp_playlist_maker.database.domain.models.Playlist
import com.example.yp_playlist_maker.database.domain.models.TracksInPlaylists
import com.example.yp_playlist_maker.search.domain.models.Track
import com.example.yp_playlist_maker.util.Converter
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlaylistFragmentViewModel(
    private val playlistsInteractor: PlaylistsInteractor,
    private val tracksInPlaylistsInteractor: TracksInPlaylistsInteractor
): ViewModel() {

    private var updatePlaylistJob: Job? = null

    private val playlistData = MutableLiveData<Playlist>()
    fun getPlaylistData(): LiveData<Playlist> = playlistData

    private val tracksInPlaylist = MutableLiveData<List<Track>>()
    fun getTracksInPlaylist(): LiveData<List<Track>> = tracksInPlaylist

    fun setPlaylistData(playlist: Playlist) {
        viewModelScope.launch {
            playlistData.value = Playlist(
                playlistId = playlist.playlistId,
                playlistName = playlist.playlistName,
                playlistDescription = playlist.playlistDescription,
                playlistCoverPath = playlist.playlistCoverPath,
                playlistSize = playlist.playlistSize,
                playlistDuration = Converter.convertMillis(playlist.playlistDuration)
            )
        }
    }

    fun setTracksInPlaylist(playlistId: Int) {
        viewModelScope.launch {
            tracksInPlaylistsInteractor
                .getTracksFromPlaylist(playlistId)
                .collect { playlistTracks ->
                    val trackList = playlistTracks.map { Converter.convertTracksInPlaylistToTrack(it) }
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
            playlistsInteractor.updatePlaylistSize(
                Playlist(
                    playlistId = playlist.playlistId,
                    playlistName = playlist.playlistName,
                    playlistDescription = playlist.playlistDescription,
                    playlistCoverPath = playlist.playlistCoverPath,
                    playlistSize = playlistSize.toString(),
                    playlistDuration = playlistDuration.toString()
                )
            )
            delay(100L)
            setTracksInPlaylist(playlist.playlistId)
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

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}