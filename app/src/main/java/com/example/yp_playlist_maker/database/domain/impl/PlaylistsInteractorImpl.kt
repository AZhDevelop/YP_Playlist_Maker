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

    override suspend fun updatePlaylist(playlist: Playlist) {
        repository.updatePlaylist(playlist)
    }

    override suspend fun getPlaylistDuration(playlistId: Int): String {
        return repository.getPlaylistDuration(playlistId)
    }

    override suspend fun getPlaylistSize(playlistId: Int): String {
        return repository.getPlaylistSize(playlistId)
    }

    override fun getPlaylistData(playlistId: Int): Flow<List<Playlist>> {
        return repository.getPlaylistData(playlistId)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        repository.deletePlaylist(playlist)
    }

}