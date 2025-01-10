package com.example.yp_playlist_maker.database.domain.impl

import com.example.yp_playlist_maker.database.domain.api.PlaylistsInteractor
import com.example.yp_playlist_maker.database.domain.api.PlaylistsRepository
import com.example.yp_playlist_maker.database.domain.models.Playlist

class PlaylistsInteractorImpl(
    private val repository: PlaylistsRepository
): PlaylistsInteractor {

    override suspend fun insertPlaylist(playlist: Playlist) {
        repository.insertPlaylist(playlist)
    }

}