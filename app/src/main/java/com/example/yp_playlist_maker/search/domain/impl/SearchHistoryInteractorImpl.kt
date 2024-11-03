package com.example.yp_playlist_maker.search.domain.impl

import com.example.yp_playlist_maker.search.domain.api.SearchHistoryInteractor
import com.example.yp_playlist_maker.search.domain.api.SearchHistoryRepository
import com.example.yp_playlist_maker.search.domain.models.Track

class SearchHistoryInteractorImpl(private val repository: SearchHistoryRepository) :
    SearchHistoryInteractor {

    override fun saveClickedTrack(track: Track, trackHistoryList: MutableList<Track>) {
        repository.saveClickedTrack(track, trackHistoryList)
    }

    override fun getHistory(): List<Track> {
        return repository.getHistory()
    }

    override fun clearHistory() {
        repository.clearHistory()
    }

}