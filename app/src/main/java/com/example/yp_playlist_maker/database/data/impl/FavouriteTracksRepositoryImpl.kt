package com.example.yp_playlist_maker.database.data.impl

import com.example.yp_playlist_maker.database.data.dao.TrackDao
import com.example.yp_playlist_maker.database.data.entity.TrackEntity
import com.example.yp_playlist_maker.database.domain.api.FavouriteTracksRepository
import com.example.yp_playlist_maker.search.domain.models.Track
import com.example.yp_playlist_maker.util.Converter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavouriteTracksRepositoryImpl(
    private val trackDao: TrackDao,
    private val converter: Converter
): FavouriteTracksRepository {

    override suspend fun insertTrack(track: Track) {
        trackDao.insertTrack(converter.convertTrackToTrackEntity(track))
    }

    override suspend fun deleteTrack(track: Track) {
        trackDao.deleteTrack(converter.convertTrackToTrackEntity(track))
    }

    override fun getFavouriteTracks(): Flow<List<Track>> = flow {
        val tracks = trackDao.getTrackList()
        emit(convertFromTrackEntity(tracks))
    }

    override suspend fun checkIsTrackFavourite(trackId: String): Boolean {
        return trackDao.checkIsTrackFavourite(trackId)
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> converter.convertTrackEntityToTrack(track) }
    }

}