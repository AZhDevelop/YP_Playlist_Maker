package com.example.yp_playlist_maker.search.data.impl

import android.util.Log
import com.example.yp_playlist_maker.search.data.dto.TrackSearchRequest
import com.example.yp_playlist_maker.search.data.dto.TrackSearchResponse
import com.example.yp_playlist_maker.search.data.network.NetworkClient
import com.example.yp_playlist_maker.search.domain.api.TrackRepository
import com.example.yp_playlist_maker.search.domain.models.Track
import com.example.yp_playlist_maker.util.Resource

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {

    override fun searchTrack(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        if (response is TrackSearchResponse) {
            when (response.resultCode) {
                -1 -> {
                    return Resource.Error(CONNECTION_ERROR)
                }
                200 -> {
                    val responseData =
                        Resource.Success((response as TrackSearchResponse).results.map {
                            Track(
                                it.trackName,
                                it.artistName,
                                it.trackTimeMillis,
                                it.artworkUrl100,
                                it.collectionName,
                                it.releaseDate,
                                it.primaryGenreName,
                                it.country,
                                it.previewUrl
                            )
                        })
                    return if (responseData.data.isNullOrEmpty()) {
                        Resource.Error(SEARCH_ERROR)
                    } else {
                        responseData
                    }
                }
                else -> {
                    return Resource.Error(SEARCH_ERROR)
                }
            }
        } else {
            return Resource.Error(SEARCH_ERROR)
        }
    }

    companion object {
        private const val CONNECTION_ERROR =
            "Проблемы со связью\nЗагрузка не удалась\nПроверьте подключение к интернету"
        private const val SEARCH_ERROR = "Ничего не нашлось"
    }
}