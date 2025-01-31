package com.example.yp_playlist_maker.database.domain.api

import com.example.yp_playlist_maker.database.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {

    suspend fun insertPlaylist(playlist: Playlist)
    fun getPlaylistList(): Flow<List<Playlist>>
    suspend fun updatePlaylist(playlist: Playlist)
    suspend fun getPlaylistSize(playlistId: Int): String
    suspend fun getPlaylistDuration(playlistId: Int): String
    suspend fun deletePlaylist(playlist: Playlist)
    fun getPlaylistData(playlistId: Int): Flow<List<Playlist>>

}