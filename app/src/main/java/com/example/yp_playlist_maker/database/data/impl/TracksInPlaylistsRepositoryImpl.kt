package com.example.yp_playlist_maker.database.data.impl

import com.example.yp_playlist_maker.database.data.dao.TracksInPlaylistsDao
import com.example.yp_playlist_maker.database.data.entity.TracksInPlaylistsEntity
import com.example.yp_playlist_maker.database.domain.api.TracksInPlaylistsRepository
import com.example.yp_playlist_maker.database.domain.models.TracksInPlaylists
import com.example.yp_playlist_maker.util.Converter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksInPlaylistsRepositoryImpl(
    private val tracksInPlaylistsDao: TracksInPlaylistsDao,
    private val converter: Converter
): TracksInPlaylistsRepository {

    override suspend fun insertTrackToPlaylist(tracksInPlaylists: TracksInPlaylists) {
        tracksInPlaylistsDao.insertTrackToPlaylist(converter.convertToTracksInPlaylistsEntity(tracksInPlaylists))
    }

    override fun getTracksFromPlaylist(playlistId: Int): Flow<List<TracksInPlaylists>> = flow {
        val tracksInPlaylists = tracksInPlaylistsDao.getTracksFromPlaylist(playlistId)
        emit(convertFromTracksInPlaylistsEntity(tracksInPlaylists))
    }

    override fun checkTrackInPlaylist(trackId: String): Flow<List<Int>> = flow {
        val playlistsList = tracksInPlaylistsDao.checkTrackInPlaylist(trackId)
        emit(playlistsList)
    }

    private fun convertFromTracksInPlaylistsEntity(tracksInPlaylistsEntity: List<TracksInPlaylistsEntity>): List<TracksInPlaylists> {
        return tracksInPlaylistsEntity.map { tracksInPlaylists ->
            converter.convertToTracksInPlaylist(tracksInPlaylists)
        }
    }

}