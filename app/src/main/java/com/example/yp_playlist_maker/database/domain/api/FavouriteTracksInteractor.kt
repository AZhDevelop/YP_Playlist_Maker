package com.example.yp_playlist_maker.database.domain.api

import com.example.yp_playlist_maker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavouriteTracksInteractor {

    suspend fun insertTrack(track: Track)
    suspend fun deleteTrack(track: Track)
    fun getFavouriteTracks(): Flow<List<Track>>
    suspend fun checkIsTrackFavourite(trackId: String): Boolean
}