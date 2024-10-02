package com.example.yp_playlist_maker

import android.app.Application
import android.content.SharedPreferences
import com.example.yp_playlist_maker.domain.models.Track
import com.example.yp_playlist_maker.presentation.application.TRACK_HISTORY_SIZE
import com.example.yp_playlist_maker.presentation.application.TRACK_KEY
import com.example.yp_playlist_maker.presentation.application.ZERO_INDEX
import com.google.gson.Gson

class SearchHistory : Application() {

    fun saveClickedTrack(
        sharedPreferences: SharedPreferences,
        track: Track,
        trackHistoryList: ArrayList<Track>,
        gson: Gson
    ) {
        if (track in trackHistoryList) {
            trackHistoryList.remove(track)
            trackHistoryList.add(ZERO_INDEX, track)
        } else if (trackHistoryList.size == TRACK_HISTORY_SIZE) {
            trackHistoryList.removeLast()
            trackHistoryList.add(ZERO_INDEX, track)
        } else {
            trackHistoryList.add(ZERO_INDEX, track)
        }
        sharedPreferences.edit()
            .putString(TRACK_KEY, gson.toJson(trackHistoryList))
            .apply()
    }

}