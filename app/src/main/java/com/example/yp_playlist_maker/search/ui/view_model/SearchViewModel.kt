package com.example.yp_playlist_maker.search.ui.view_model

import androidx.lifecycle.ViewModel
import com.example.yp_playlist_maker.search.domain.api.SearchHistoryInteractor
import com.example.yp_playlist_maker.search.domain.api.TrackInteractor

class SearchViewModel(
    private val searchTrackService: TrackInteractor,
    private val searchHistoryService: SearchHistoryInteractor
): ViewModel() {

}