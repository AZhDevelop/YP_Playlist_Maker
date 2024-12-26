package com.example.yp_playlist_maker.database.data

import com.example.yp_playlist_maker.database.data.converters.TrackDbConverter
import com.example.yp_playlist_maker.database.data.entity.TrackEntity
import com.example.yp_playlist_maker.database.domain.FavouriteTracksRepository
import com.example.yp_playlist_maker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavouriteTracksRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConverter: TrackDbConverter
): FavouriteTracksRepository {

    override fun getFavouriteTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.trackDao().getTrackList()
        emit(convertFromTrackEntity(tracks))
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConverter.map(track) }
    }

}