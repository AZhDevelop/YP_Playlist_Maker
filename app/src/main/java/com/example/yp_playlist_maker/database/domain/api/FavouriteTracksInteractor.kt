package com.example.yp_playlist_maker.database.domain.api

import com.example.yp_playlist_maker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavouriteTracksInteractor {

    fun getFavouriteTracks(): Flow<List<Track>>

}