package com.example.yp_playlist_maker.search.ui.view_model

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.yp_playlist_maker.search.domain.api.SearchHistoryInteractor
import com.example.yp_playlist_maker.search.domain.api.TrackInteractor
import com.example.yp_playlist_maker.search.domain.models.Track
import com.example.yp_playlist_maker.util.Constants

class SearchViewModel(
    private val searchTrackService: TrackInteractor,
    private val searchHistoryService: SearchHistoryInteractor
): ViewModel() {

    private val handler = Handler(Looper.getMainLooper())
    private val trackList: MutableList<Track> = mutableListOf()
    private val trackHistoryList: MutableList<Track> = mutableListOf()
    private val trackListData = MutableLiveData<MutableList<Track>>()

    fun getTrackListLiveData(): LiveData<MutableList<Track>> = trackListData

    fun getTrackList() : List<Track> {
        return trackList
    }

    fun getTrackHistoryList() : List<Track> {
        return trackHistoryList
    }

    fun getTrackHistory() {
        val trackHistory = searchHistoryService.getHistory()
        Log.d("log", "${trackHistory.size}")
        if (trackHistory.isNotEmpty()) {
            trackList.clear()
            trackHistoryList.clear()
            trackList.addAll(trackHistory)
            trackHistoryList.addAll(trackHistory)
            trackListData.value = trackList
        }
    }

    init {
        getTrackHistory()
    }

    fun clearTrackList() {
        trackList.clear()
        trackListData.value?.clear()
    }

    fun updateTrackList() {
        trackList.clear()
        trackList.addAll(trackHistoryList)
        trackListData.value = trackList
    }

    private val searchState = MutableLiveData<Constants.SearchState>()
    fun getSearchStatus(): LiveData<Constants.SearchState> = searchState

    fun search(expression: String) {

        searchState.value = Constants.SearchState.LOADING

        searchTrackService.searchTrack(expression) { result ->
            handler.post {
                when (result) {
                    is TrackInteractor.TrackResult.Success -> {
                        trackList.clear()
                        trackList.addAll(result.tracks)
                        trackListData.value = trackList
                        searchState.value = Constants.SearchState.SUCCESS
                    }
                    is TrackInteractor.TrackResult.Error -> {
                        if (result.message == Constants.SearchState.SEARCH_ERROR) {
                            searchState.value = Constants.SearchState.SEARCH_ERROR
                        } else if (result.message == Constants.SearchState.CONNECTION_ERROR) {
                            searchState.value = Constants.SearchState.CONNECTION_ERROR
                        }
                    }
                }
            }
        }
    }

    // Работа с историей
    fun clearHistory() {
        searchHistoryService.clearHistory()
        trackList.clear()
        trackHistoryList.clear()
        trackListData.value?.clear()
    }

    fun saveClickedTrack(track: Track) {
        searchHistoryService.saveClickedTrack(track, trackHistoryList)
        trackHistoryList.clear()
        trackHistoryList.addAll(searchHistoryService.updateTrackHistoryList())
    }

}