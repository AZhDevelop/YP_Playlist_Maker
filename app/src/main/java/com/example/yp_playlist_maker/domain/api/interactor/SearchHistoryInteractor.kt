package com.example.yp_playlist_maker.domain.api.interactor

import com.example.yp_playlist_maker.domain.models.Track

interface SearchHistoryInteractor {
    fun saveClickedTrack(track: Track, trackHistoryList: ArrayList<Track>)
    fun getHistory() : ArrayList<Track>
    fun clearHistory()
}