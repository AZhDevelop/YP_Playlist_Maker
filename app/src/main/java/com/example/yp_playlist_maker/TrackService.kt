package com.example.yp_playlist_maker

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TrackService {
    private val trackUrl: String = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(trackUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val trackService: TrackApi = retrofit.create(TrackApi::class.java)
}