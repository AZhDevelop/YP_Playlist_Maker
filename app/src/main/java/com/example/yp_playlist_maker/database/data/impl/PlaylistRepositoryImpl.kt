package com.example.yp_playlist_maker.database.data.impl

import com.example.yp_playlist_maker.database.data.dao.PlaylistDao
import com.example.yp_playlist_maker.database.domain.api.PlaylistsRepository
import com.example.yp_playlist_maker.database.domain.models.Playlist
import com.example.yp_playlist_maker.util.Converter

class PlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val converter: Converter
): PlaylistsRepository {

    override suspend fun insertPlaylist(playlist: Playlist) {
        playlistDao.insertPlaylist(converter.convertPlaylistToPlaylistEntity(playlist))
    }

}