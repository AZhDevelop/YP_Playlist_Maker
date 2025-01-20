package com.example.yp_playlist_maker.database.domain.impl

import com.example.yp_playlist_maker.database.domain.api.TracksInPlaylistsInteractor
import com.example.yp_playlist_maker.database.domain.api.TracksInPlaylistsRepository
import com.example.yp_playlist_maker.database.domain.models.TracksInPlaylists
import kotlinx.coroutines.flow.Flow

class TracksInPlaylistsInteractorImpl(
    private val repository: TracksInPlaylistsRepository
): TracksInPlaylistsInteractor {

    override suspend fun insertTrackToPlaylist(tracksInPlaylists: TracksInPlaylists) {
        repository.insertTrackToPlaylist(tracksInPlaylists)
    }

    override fun getTracksFromPlaylist(playlistId: Int): Flow<List<TracksInPlaylists>> {
        return repository.getTracksFromPlaylist(playlistId)
    }
}