package com.example.yp_playlist_maker.search.domain.api

import com.example.yp_playlist_maker.search.domain.models.Track
import com.example.yp_playlist_maker.util.State
import kotlinx.coroutines.flow.Flow

interface TrackInteractor {
    fun searchTrack(expression: String) : Flow<Pair<List<Track>?, State.SearchState?>>
}