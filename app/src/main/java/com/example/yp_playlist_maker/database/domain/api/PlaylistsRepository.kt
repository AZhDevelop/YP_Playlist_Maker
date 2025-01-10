package com.example.yp_playlist_maker.database.domain.api

import com.example.yp_playlist_maker.database.domain.models.Playlist

interface PlaylistsRepository {

    suspend fun insertPlaylist(playlist: Playlist)

}