package com.example.yp_playlist_maker.search.domain.api

import com.example.yp_playlist_maker.search.domain.models.Track
import com.example.yp_playlist_maker.util.State

interface TrackInteractor {

    sealed class TrackResult {
        data class Success(val tracks: List<Track>) : TrackResult()
        data class Error(val message: State.SearchState) : TrackResult()
    }

    fun searchTrack(expression: String, consumer: (TrackResult) -> Unit)

    companion object {
        enum class SearchState
    }

}