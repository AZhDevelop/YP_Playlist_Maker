package com.example.yp_playlist_maker

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson

class SearchHistory : Application() {

    fun saveClickedTrack(sharedPreferences: SharedPreferences, track: Track, trackList: ArrayList<Track>) {
        trackList.add(track)
        sharedPreferences.edit()
              .putString("track_key", Gson().toJson(trackList))
              .apply()
        Log.d("save_click", "$trackList")
    }

}