package com.example.yp_playlist_maker.search.data.network

import com.example.yp_playlist_maker.search.data.dto.TrackSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TrackApi {

    @GET("/search?entity=song")
    suspend fun searchTrack(@Query("term") text: String): TrackSearchResponse
}