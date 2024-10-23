package com.example.yp_playlist_maker.search.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yp_playlist_maker.creator.Creator
import com.example.yp_playlist_maker.player.ui.view_model.AudioPlayerViewModel

class SearchViewModelFactory: ViewModelProvider.Factory {

    private val searchTrackService = Creator.provideTrackInteractor()
    private val searchHistoryService = Creator.provideSeacrhHistoryInteractor()

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(searchTrackService, searchHistoryService) as T
    }
}