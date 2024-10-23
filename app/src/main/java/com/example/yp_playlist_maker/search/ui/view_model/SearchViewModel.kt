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
        Log.d("Init", "init")
    }

    private val progressBarVisibility = MutableLiveData<Boolean>()
    fun getProgressBarVisibility(): LiveData<Boolean> = progressBarVisibility

    private val recyclerViewVisibility = MutableLiveData<Boolean>()
    fun getRecyclerViewVisibility(): LiveData<Boolean> = recyclerViewVisibility

    private val errorText = MutableLiveData<String>()
    fun getErrorText() : LiveData<String> = errorText

    fun search(expression: String, trackList: ArrayList<Track>, adapter: TrackAdapter) {

        progressBarVisibility.value = true

        searchTrackService.searchTrack(expression, object : TrackInteractor.TrackConsumer {
            override fun consume(foundTrack: List<Track>?, errorMessage: String?) {
                handler.post {
                    progressBarVisibility.value = false
                    trackList.clear()
                    if (foundTrack != null) {
                        trackList.addAll(foundTrack)
                        adapter.notifyDataSetChanged()
//                        checkPlaceholder()
                        recyclerViewVisibility.value = true
                    }
                    if (errorMessage != null) {
                        errorText.value = errorMessage
                    }
                }
            }
        })
    }

}