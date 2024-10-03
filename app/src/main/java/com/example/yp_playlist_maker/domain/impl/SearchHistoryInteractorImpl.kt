package com.example.yp_playlist_maker.domain.impl

import com.example.yp_playlist_maker.domain.api.interactor.SearchHistoryInteractor
import com.example.yp_playlist_maker.domain.api.repository.SearchHistoryRepository
import com.example.yp_playlist_maker.domain.models.Track

class SearchHistoryInteractorImpl(private val repository: SearchHistoryRepository) : SearchHistoryInteractor {

    override fun saveClickedTrack(track: Track, trackHistoryList: ArrayList<Track>) {
        repository.saveClickedTrack(track, trackHistoryList)
    }

    override fun getHistory(): ArrayList<Track> {
        return repository.getHistory()
    }

    override fun clearHistory() {
        repository.clearHistory()
    }

}