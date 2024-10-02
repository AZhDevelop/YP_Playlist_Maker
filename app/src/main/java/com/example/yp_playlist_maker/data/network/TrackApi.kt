package com.example.yp_playlist_maker.data.network

import com.example.yp_playlist_maker.data.dto.TrackSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TrackApi {

    @GET("/search?entity=song")
    fun searchTrack(@Query("term") text: String): Call<TrackSearchResponse>
}