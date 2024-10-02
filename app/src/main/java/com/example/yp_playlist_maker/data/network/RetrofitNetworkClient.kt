package com.example.yp_playlist_maker.data.network

import com.example.yp_playlist_maker.data.dto.Response
import com.example.yp_playlist_maker.data.dto.TrackSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {
    private val trackUrl: String = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(trackUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val trackService: TrackApi = retrofit.create(TrackApi::class.java)

    override fun doRequest(dto: Any): Response {
        if (dto is TrackSearchRequest) {
            val response = trackService.searchTrack(dto.expression).execute()
            val body = response.body() ?: Response()
            return body.apply { resultCode = response.code() }
        } else {
            return Response().apply { resultCode = ERROR_400 }
        }
    }

    companion object {
        private const val ERROR_400 = 400
    }

}