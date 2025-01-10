package com.example.yp_playlist_maker.playlist.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yp_playlist_maker.database.domain.api.PlaylistsInteractor
import com.example.yp_playlist_maker.database.domain.impl.PlaylistsInteractorImpl
import com.example.yp_playlist_maker.database.domain.models.Playlist
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playlistsInteractor: PlaylistsInteractor
): ViewModel() {

    fun createPlaylist(playlist: Playlist) {
        viewModelScope.launch {
            playlistsInteractor.insertPlaylist(playlist)
        }
    }

}