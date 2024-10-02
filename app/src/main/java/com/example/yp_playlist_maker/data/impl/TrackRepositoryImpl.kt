package com.example.yp_playlist_maker.data.impl

import com.example.yp_playlist_maker.data.dto.TrackSearchRequest
import com.example.yp_playlist_maker.data.dto.TrackSearchResponse
import com.example.yp_playlist_maker.data.network.NetworkClient
import com.example.yp_playlist_maker.domain.api.repository.TrackRepository
import com.example.yp_playlist_maker.domain.models.Track

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {

    override fun searchTrack(expression: String): List<Track> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        if (response.resultCode == 200) {
            return (response as TrackSearchResponse).results.map {
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
            }
        } else {
            return  emptyList()
        }
    }

}