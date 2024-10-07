package com.example.yp_playlist_maker.data.impl

import android.content.Context
import com.example.yp_playlist_maker.domain.api.repository.SearchHistoryRepository
import com.example.yp_playlist_maker.domain.models.Track
import com.google.gson.Gson

class SearchHistoryRepositoryImpl(context: Context) : SearchHistoryRepository {

    private val sharedPreferences =
        context.getSharedPreferences(TRACK_LIST_KEY, Context.MODE_PRIVATE)
    private val gson: Gson = Gson()

    override fun saveClickedTrack(track: Track, trackHistoryList: ArrayList<Track>) {
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

    override fun getHistory(): ArrayList<Track> {
        val resultTrackList: ArrayList<Track> = arrayListOf()
        val trackHistory = sharedPreferences.getString(TRACK_KEY, EMPTY_STRING)
        if (trackHistory != "") {
            val trackHistoryJson = gson.fromJson(trackHistory, Array<Track>::class.java)
            resultTrackList.addAll(trackHistoryJson)
            return resultTrackList
        } else {
            return resultTrackList
        }
    }

    override fun clearHistory() {
        sharedPreferences.edit()
            .clear()
            .apply()
    }

    companion object {
        private const val TRACK_LIST_KEY: String = "track_list_key"
        private const val TRACK_KEY: String = "track_key"
        private const val EMPTY_STRING: String = ""
        private const val TRACK_HISTORY_SIZE: Int = 10
        const val ZERO_INDEX: Int = 0
    }
}