package com.example.yp_playlist_maker.search.data.network

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.yp_playlist_maker.search.data.dto.Response
import com.example.yp_playlist_maker.search.data.dto.TrackSearchRequest
import com.example.yp_playlist_maker.search.data.dto.TrackSearchResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(
    private val connectivityManager: ConnectivityManager,
    private val trackApi: TrackApi
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        if (!waitForConnection()) {
            return Response(resultCode = CONNECTION_ERROR_CODE)
        }
        return withContext(Dispatchers.IO) {
            try {
                if (dto is TrackSearchRequest) {
                    val response = trackApi.searchTrack(dto.expression)
                    TrackSearchResponse(response.results, SUCCESS_CODE)
                } else {
                    Response(resultCode = EXPRESSION_ERROR_CODE)
                }
            } catch (e: Exception) {
                Response(resultCode = CONNECTION_ERROR_CODE)
            }
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

    private suspend fun waitForConnection(): Boolean {
        repeat(RETRIES) {
            if (isConnected()) {
                return true
            }
            delay(DELAY_MS)
        }
        return false
    }

    companion object {
        private const val CONNECTION_ERROR_CODE = -1
        private const val EXPRESSION_ERROR_CODE = 400
        private const val RETRIES = 3
        private const val DELAY_MS: Long = 1000
        private const val SUCCESS_CODE: Int = 200
    }

}