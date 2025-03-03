package com.example.yp_playlist_maker.database.data.impl

import com.example.yp_playlist_maker.database.data.dao.PlaylistDao
import com.example.yp_playlist_maker.database.data.entity.PlaylistEntity
import com.example.yp_playlist_maker.database.domain.api.PlaylistsRepository
import com.example.yp_playlist_maker.database.domain.models.Playlist
import com.example.yp_playlist_maker.util.Converter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val converter: Converter
): PlaylistsRepository {

    override suspend fun insertPlaylist(playlist: Playlist) {
        playlistDao.insertPlaylist(converter.convertPlaylistToPlaylistEntity(playlist))
    }

    override fun getPlaylistList(): Flow<List<Playlist>> = flow {
        val playlists = playlistDao.getPlaylistList()
        emit(convertFromPlaylistEntity(playlists))
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistDao.updatePlaylist(converter.convertPlaylistToPlaylistEntity(playlist))
    }

    override suspend fun getPlaylistSize(playlistId: Int): String {
        return playlistDao.getPlaylistSize(playlistId)
    }

    override suspend fun getPlaylistDuration(playlistId: Int): String {
        return playlistDao.getPlaylistDuration(playlistId)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlistDao.deletePlaylist(converter.convertPlaylistToPlaylistEntity(playlist))
    }

    override fun getPlaylistData(playlistId: Int): Flow<List<Playlist>> = flow {
        val playlist = playlistDao.getPlaylistData(playlistId)
        emit(convertFromPlaylistEntity(playlist))
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlist -> converter.convertPlaylistEntityToPlaylist(playlist) }
    }

}