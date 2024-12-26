package com.example.yp_playlist_maker.database.domain

import com.example.yp_playlist_maker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavouriteTracksRepository {

    fun getFavouriteTracks(): Flow<List<Track>>

}