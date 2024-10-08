package com.example.yp_playlist_maker.data.impl

import android.content.res.Resources
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.data.dto.TrackSearchRequest
import com.example.yp_playlist_maker.data.dto.TrackSearchResponse
import com.example.yp_playlist_maker.data.network.NetworkClient
import com.example.yp_playlist_maker.domain.api.repository.TrackRepository
import com.example.yp_playlist_maker.domain.models.Track
import com.example.yp_playlist_maker.util.Resource

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {

    override fun searchTrack(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        when (response.resultCode) {
            -1 -> {
                return Resource.Error(CONNECTION_ERROR)
            }
            200 -> {
                val responseData = Resource.Success((response as TrackSearchResponse).results.map {
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
            } else -> {
                return Resource.Error(SERVER_ERROR)
            }
        }
    }

    companion object {
        private const val CONNECTION_ERROR =
            "Проблемы со связью\nЗагрузка не удалась\nПроверьте подключение к интернету"
        private const val SERVER_ERROR = "Ошибка сервера"
        private const val SEARCH_ERROR = "Ничего не нашлось"
    }
}