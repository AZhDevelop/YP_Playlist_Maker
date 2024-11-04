package com.example.yp_playlist_maker.search.data.impl

import android.content.SharedPreferences
import com.example.yp_playlist_maker.search.domain.api.SearchHistoryRepository
import com.example.yp_playlist_maker.search.domain.models.Track
import com.example.yp_playlist_maker.util.Constants
import com.google.gson.Gson

class SearchHistoryRepositoryImpl(private val sharedPreferences: SharedPreferences) : SearchHistoryRepository {

    private val gson: Gson = Gson()
    private val mutableTrackHistoryList: MutableList<Track> = mutableListOf()

    override fun saveClickedTrack(track: Track, trackHistoryList: List<Track>) {

        mutableTrackHistoryList.clear()
        mutableTrackHistoryList.addAll(trackHistoryList)

        if (track in mutableTrackHistoryList) {
            mutableTrackHistoryList.remove(track)
            mutableTrackHistoryList.add(Constants.ZERO_INDEX, track)
        } else if (mutableTrackHistoryList.size == Constants.TRACK_HISTORY_SIZE) {
            mutableTrackHistoryList.removeLast()
            mutableTrackHistoryList.add(Constants.ZERO_INDEX, track)
        } else {
            mutableTrackHistoryList.add(Constants.ZERO_INDEX, track)
        }
        sharedPreferences.edit()
            .putString(Constants.TRACK_KEY, gson.toJson(mutableTrackHistoryList))
            .apply()
    }

    override fun updateTrackHistoryList(): List<Track> {
        return mutableTrackHistoryList
    }

    override fun getHistory(): List<Track> {
        val resultTrackList: MutableList<Track> = mutableListOf()
        val trackHistory = sharedPreferences.getString(Constants.TRACK_KEY, Constants.EMPTY_STRING)
        if (trackHistory != Constants.EMPTY_STRING) {
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

}