package com.example.yp_playlist_maker.database.domain.api

import com.example.yp_playlist_maker.database.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {

    suspend fun insertPlaylist(playlist: Playlist)
    fun getPlaylistList(): Flow<List<Playlist>>

}