package com.example.yp_playlist_maker.database.domain.impl

import com.example.yp_playlist_maker.database.domain.api.PlaylistsInteractor
import com.example.yp_playlist_maker.database.domain.api.PlaylistsRepository
import com.example.yp_playlist_maker.database.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

class PlaylistsInteractorImpl(
    private val repository: PlaylistsRepository
): PlaylistsInteractor {

    override suspend fun insertPlaylist(playlist: Playlist) {
        repository.insertPlaylist(playlist)
    }

    override fun getPlaylistList(): Flow<List<Playlist>> {
        return repository.getPlaylistList()
    }

    override suspend fun updatePlaylistSize(playlist: Playlist) {
        repository.updatePlaylistSize(playlist)
    }

    override suspend fun getPlaylistDuration(playlistId: Int): String {
        return repository.getPlaylistDuration(playlistId)
    }

    override suspend fun getPlaylistSize(playlistId: Int): String {
        return repository.getPlaylistSize(playlistId)
    }

}