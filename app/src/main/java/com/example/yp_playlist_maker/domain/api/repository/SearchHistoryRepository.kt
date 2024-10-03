package com.example.yp_playlist_maker.domain.api.repository

import com.example.yp_playlist_maker.domain.models.Track

interface SearchHistoryRepository {
    fun saveClickedTrack(track: Track, trackHistoryList: ArrayList<Track>)
    fun getHistory() : ArrayList<Track>
    fun clearHistory()
}