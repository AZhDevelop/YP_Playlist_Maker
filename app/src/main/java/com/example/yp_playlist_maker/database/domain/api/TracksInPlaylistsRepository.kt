package com.example.yp_playlist_maker.database.domain.api

import com.example.yp_playlist_maker.database.domain.models.TracksInPlaylists
import kotlinx.coroutines.flow.Flow

interface TracksInPlaylistsRepository {

    suspend fun insertTrackToPlaylist(tracksInPlaylists: TracksInPlaylists)
    fun getTracksFromPlaylist(playlistId: Int): Flow<List<TracksInPlaylists>>

}