package com.example.yp_playlist_maker.database.domain.impl

import com.example.yp_playlist_maker.database.data.entity.PlaylistEntity
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

    override suspend fun updateTrackIdList(playlist: Playlist) {
        repository.updateTrackIdList(playlist)
    }

    override suspend fun getTrackIdList(playlistName: String): String {
        return repository.getTrackIdList(playlistName)
    }

}