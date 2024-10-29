package com.example.yp_playlist_maker.search.ui.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.yp_playlist_maker.search.domain.api.SearchHistoryInteractor
import com.example.yp_playlist_maker.search.domain.api.TrackInteractor
import com.example.yp_playlist_maker.search.domain.models.Track
import com.example.yp_playlist_maker.search.ui.TrackAdapter

class SearchViewModel(
    private val searchTrackService: TrackInteractor,
    private val searchHistoryService: SearchHistoryInteractor
): ViewModel() {

    private val handler = Handler(Looper.getMainLooper())

    private val trackHistory = MutableLiveData<ArrayList<Track>>()
    fun getTrackHistory(): LiveData<ArrayList<Track>> = trackHistory
    private fun initTrackHistory() {
        trackHistory.value = searchHistoryService.getHistory()
    }
    fun reloadTrackHistory() {
        trackHistory.value = searchHistoryService.getHistory()
    }

    init {
        initTrackHistory()
    }

    private val searchStatus = MutableLiveData<String>()
    fun getSearchStatus(): LiveData<String> = searchStatus

    fun search(expression: String, trackList: ArrayList<Track>, adapter: TrackAdapter) {

        searchStatus.value = LOADING

        searchTrackService.searchTrack(expression, object : TrackInteractor.TrackConsumer {
            override fun consume(foundTrack: List<Track>?, errorMessage: String?) {
                handler.post {
                    if (foundTrack != null && errorMessage.isNullOrEmpty()) {
                        trackList.clear()
                        trackList.addAll(foundTrack)
                        adapter.notifyDataSetChanged()
                        searchStatus.value = SUCCESS
                    } else if (errorMessage == SEARCH_ERROR) {
                        searchStatus.value = SEARCH_ERROR
                    } else if (errorMessage == CONNECTION_ERROR) {
                        searchStatus.value = CONNECTION_ERROR
                    }
                }
            }
        })
    }

    // Работа с историей
    fun clearHistory() {
        searchHistoryService.clearHistory()
    }

    fun saveClickedTrack(track: Track, trackHistoryList: ArrayList<Track>) {
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