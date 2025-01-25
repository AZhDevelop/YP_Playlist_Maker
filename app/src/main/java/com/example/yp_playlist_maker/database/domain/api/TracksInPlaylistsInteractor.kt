package com.example.yp_playlist_maker.database.domain.api

import com.example.yp_playlist_maker.database.domain.models.TracksInPlaylists
import kotlinx.coroutines.flow.Flow

interface TracksInPlaylistsInteractor {

    suspend fun insertTrackToPlaylist(tracksInPlaylists: TracksInPlaylists)
    fun getTracksFromPlaylist(playlistId: Int): Flow<List<TracksInPlaylists>>
    fun checkTrackInPlaylist(trackId: String): Flow<List<Int>>

}