package com.example.yp_playlist_maker.database.domain.impl

import com.example.yp_playlist_maker.database.domain.api.FavouriteTracksInteractor
import com.example.yp_playlist_maker.database.domain.api.FavouriteTracksRepository
import com.example.yp_playlist_maker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavouriteTracksInteractorImpl(
    private val repository: FavouriteTracksRepository
): FavouriteTracksInteractor {

    override fun getFavouriteTracks(): Flow<List<Track>> {
        return repository.getFavouriteTracks()
    }

}