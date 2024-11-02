package com.example.yp_playlist_maker.search.data.network

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.yp_playlist_maker.search.data.dto.Response
import com.example.yp_playlist_maker.search.data.dto.TrackSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient(private val connectivityManager: ConnectivityManager) : NetworkClient {
    private val trackUrl: String = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(trackUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val trackService: TrackApi = retrofit.create(TrackApi::class.java)

    override fun doRequest(dto: Any): Response {
        if (!waitForConnection()) {
            return Response().apply { resultCode = CONNECTION_ERROR }
        }
        if (!isConnected()) {
            return Response().apply { resultCode = CONNECTION_ERROR }
        }
        try {
            if (dto is TrackSearchRequest) {
                val response = trackService.searchTrack(dto.expression).execute()
                val body = response.body() ?: Response()
                return body.apply { resultCode = response.code() }
            } else {
                return Response().apply { resultCode = ERROR_400 }
            }
        } catch (e: Exception) {
            return Response().apply { resultCode = CONNECTION_ERROR }
        }
    }

    private fun isConnected() : Boolean {
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }

    private fun waitForConnection(): Boolean {
        repeat(RETRIES) {
            if (isConnected()) {
                return true
            }
            Thread.sleep(DELAY_MS)
        }
        return false
    }

    companion object {
        private const val CONNECTION_ERROR = -1
        private const val ERROR_400 = 400
        private const val RETRIES = 3
        private const val DELAY_MS: Long = 1000
    }

}