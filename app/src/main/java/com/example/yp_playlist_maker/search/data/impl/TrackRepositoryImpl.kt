package com.example.yp_playlist_maker.search.data.impl

import com.example.yp_playlist_maker.search.data.dto.TrackSearchRequest
import com.example.yp_playlist_maker.search.data.dto.TrackSearchResponse
import com.example.yp_playlist_maker.search.data.network.NetworkClient
import com.example.yp_playlist_maker.search.domain.api.TrackRepository
import com.example.yp_playlist_maker.search.domain.models.Track
import com.example.yp_playlist_maker.util.State
import com.example.yp_playlist_maker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {

    override fun searchTrack(expression: String): Flow<Resource> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        when (response.resultCode) {
            CONNECTION_ERROR_CODE -> {
                emit(Resource.Error(State.SearchState.CONNECTION_ERROR))
            }
            SUCCESS_CODE -> {
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
                    emit(if (responseData.data.isEmpty()) {
                        Resource.Error(State.SearchState.SEARCH_ERROR)
                    } else {
                        responseData
                    })
                } else {
                    emit(Resource.Error(State.SearchState.SEARCH_ERROR))
                }
            }
            else -> {
                emit(Resource.Error(State.SearchState.SEARCH_ERROR))
            }
        }
    }

    companion object {
        private const val CONNECTION_ERROR_CODE = -1
        private const val SUCCESS_CODE: Int = 200
    }

}