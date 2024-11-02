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

class SearchViewModel(
    private val searchTrackService: TrackInteractor,
    private val searchHistoryService: SearchHistoryInteractor
): ViewModel() {

    private val handler = Handler(Looper.getMainLooper())
    private val trackList: ArrayList<Track> = arrayListOf()
    private val trackHistoryList: ArrayList<Track> = arrayListOf()
    private val trackListData = MutableLiveData<ArrayList<Track>>()
    fun getTrackListLiveData(): LiveData<ArrayList<Track>> = trackListData

    fun getTrackList() : ArrayList<Track> {
        return trackList
    }

    fun getTrackHistoryList() : ArrayList<Track> {
        return trackHistoryList
    }

    fun getTrackHistory() {
        Log.d("onFocus", "getTrackHistory")
        val trackHistory = searchHistoryService.getHistory()
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

    private val searchStatus = MutableLiveData<String>()
    fun getSearchStatus(): LiveData<String> = searchStatus

    fun search(expression: String) {

        searchStatus.value = LOADING

        searchTrackService.searchTrack(expression) { result ->
            handler.post {
                when (result) {
                    is TrackInteractor.TrackResult.Success -> {
                        trackList.clear()
                        trackList.addAll(result.tracks)
                        trackListData.value = trackList
                        searchStatus.value = SUCCESS
                    }
                    is TrackInteractor.TrackResult.Error -> {
                        if (result.message == SEARCH_ERROR) {
                            searchStatus.value = SEARCH_ERROR
                        } else if (result.message == CONNECTION_ERROR) {
                            searchStatus.value = CONNECTION_ERROR
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
    }

    companion object {
        private const val LOADING = "Loading"
        private const val SUCCESS = "Success"
        private const val CONNECTION_ERROR =
            "Проблемы со связью\nЗагрузка не удалась\nПроверьте подключение к интернету"
        private const val SEARCH_ERROR = "Ничего не нашлось"
    }

}