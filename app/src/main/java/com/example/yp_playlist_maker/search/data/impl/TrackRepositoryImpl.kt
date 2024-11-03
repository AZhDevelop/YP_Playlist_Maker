package com.example.yp_playlist_maker.search.data.impl

import com.example.yp_playlist_maker.search.data.dto.TrackSearchRequest
import com.example.yp_playlist_maker.search.data.dto.TrackSearchResponse
import com.example.yp_playlist_maker.search.data.network.NetworkClient
import com.example.yp_playlist_maker.search.domain.api.TrackRepository
import com.example.yp_playlist_maker.search.domain.models.Track
import com.example.yp_playlist_maker.util.Constants
import com.example.yp_playlist_maker.util.Resource

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {

    override fun searchTrack(expression: String): Resource {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        when (response.resultCode) {
            -1 -> {
                return Resource.Error(Constants.SearchStatus.CONNECTION_ERROR)
            }
            200 -> {
                if (response is TrackSearchResponse) {
                    val responseData = Resource.Success((response).results.map {
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
                    return if (responseData.data.isEmpty()) {
                        Resource.Error(Constants.SearchStatus.SEARCH_ERROR)
                    } else {
                        responseData
                    }
                } else {
                    return Resource.Error(Constants.SearchStatus.SEARCH_ERROR)
                }
            }
            else -> {
                return Resource.Error(Constants.SearchStatus.SEARCH_ERROR)
            }
        }
    }
}