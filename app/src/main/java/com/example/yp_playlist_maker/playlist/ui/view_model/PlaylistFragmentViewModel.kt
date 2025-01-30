package com.example.yp_playlist_maker.playlist.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yp_playlist_maker.database.domain.api.TracksInPlaylistsInteractor
import com.example.yp_playlist_maker.database.domain.models.Playlist
import com.example.yp_playlist_maker.search.domain.models.Track
import com.example.yp_playlist_maker.util.Converter
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class PlaylistFragmentViewModel(
    private val playlistService: TracksInPlaylistsInteractor
): ViewModel() {

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
            playlistService
                .getTracksFromPlaylist(playlistId)
                .collect { playlistTracks ->
                    val trackList = playlistTracks.map { Converter.convertTracksInPlaylistToTrack(it) }
                    tracksInPlaylist.value = trackList
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}