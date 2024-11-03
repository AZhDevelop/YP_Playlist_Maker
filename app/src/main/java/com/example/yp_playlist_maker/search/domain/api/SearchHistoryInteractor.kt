package com.example.yp_playlist_maker.search.domain.api

import com.example.yp_playlist_maker.search.domain.models.Track

interface SearchHistoryInteractor {
    fun saveClickedTrack(track: Track, trackHistoryList: MutableList<Track>)
    fun getHistory() : List<Track>
    fun clearHistory()
}