package com.example.yp_playlist_maker.search.domain.api

import com.example.yp_playlist_maker.search.domain.models.Track

interface SearchHistoryRepository {
    fun saveClickedTrack(track: Track, trackHistoryList: List<Track>)
    fun updateTrackHistoryList(): List<Track>
    fun getHistory() : List<Track>
    fun clearHistory()
}