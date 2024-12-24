package com.example.yp_playlist_maker.search.domain.impl

import com.example.yp_playlist_maker.search.domain.api.TrackInteractor
import com.example.yp_playlist_maker.search.domain.api.TrackRepository
import com.example.yp_playlist_maker.search.domain.models.Track
import com.example.yp_playlist_maker.util.Resource
import com.example.yp_playlist_maker.util.State
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.Executors

class TrackInteractorImpl(private val repository: TrackRepository) : TrackInteractor {

    override fun searchTrack(expression: String): Flow<Pair<List<Track>?, State.SearchState?>> {
        return repository.searchTrack(expression).map { result ->
            when(result) {
                is Resource.Success<*> -> Pair(result.data as List<Track>, null)
                is Resource.Error -> Pair(null, result.message)
            }
        }
    }

}