package com.example.yp_playlist_maker.search.data.impl

import android.content.SharedPreferences
import com.example.yp_playlist_maker.search.domain.api.SearchHistoryRepository
import com.example.yp_playlist_maker.search.domain.models.Track
import com.google.gson.Gson

class SearchHistoryRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) : SearchHistoryRepository {

    private val mutableTrackHistoryList: MutableList<Track> = mutableListOf()

    override fun saveClickedTrack(track: Track, trackHistoryList: List<Track>) {

        mutableTrackHistoryList.clear()
        mutableTrackHistoryList.addAll(trackHistoryList)

        if (track in mutableTrackHistoryList) {
            mutableTrackHistoryList.remove(track)
            mutableTrackHistoryList.add(ZERO_INDEX, track)
        } else if (mutableTrackHistoryList.size == TRACK_HISTORY_SIZE) {
            mutableTrackHistoryList.removeLast()
            mutableTrackHistoryList.add(ZERO_INDEX, track)
        } else {
            mutableTrackHistoryList.add(ZERO_INDEX, track)
        }
        sharedPreferences.edit()
            .putString(TRACK_KEY, gson.toJson(mutableTrackHistoryList))
            .apply()
    }

    override fun updateTrackHistoryList(): List<Track> {
        return mutableTrackHistoryList
    }

    override fun getHistory(): List<Track> {
        val resultTrackList: MutableList<Track> = mutableListOf()
        val trackHistory = sharedPreferences.getString(TRACK_KEY, EMPTY_STRING)
        if (trackHistory != EMPTY_STRING) {
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
        private const val EMPTY_STRING: String = ""
        private const val TRACK_HISTORY_SIZE: Int = 10
        private const val ZERO_INDEX: Int = 0
        private const val TRACK_KEY: String = "track_key"
    }

}