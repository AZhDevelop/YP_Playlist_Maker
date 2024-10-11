package com.example.yp_playlist_maker.search.domain.api

import com.example.yp_playlist_maker.search.domain.models.Track

interface SearchHistoryRepository {
    fun saveClickedTrack(track: Track, trackHistoryList: ArrayList<Track>)
    fun getHistory() : ArrayList<Track>
    fun clearHistory()
}