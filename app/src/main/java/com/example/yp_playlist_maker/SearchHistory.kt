package com.example.yp_playlist_maker

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson

class SearchHistory : Application() {

    fun saveClickedTrack(sharedPreferences: SharedPreferences, track: Track, trackList: ArrayList<Track>) {
        if ((trackList.size == 10) and (track in trackList)) {
            trackList.remove(track)
            trackList.add(0, track)
        } else if (trackList.size == 10) {
            trackList.removeLast()
            if (track in trackList) {
                trackList.remove(track)
                trackList.add(0, track)
            } else {
                trackList.add(0, track)
            }
        } else if (track in trackList) {
            trackList.remove(track)
            trackList.add(0, track)
        } else {
            trackList.add(0, track)
        }
        sharedPreferences.edit()
              .putString(TRACK_KEY, Gson().toJson(trackList))
              .apply()
        Log.d("save_click", "$trackList")
    }

}