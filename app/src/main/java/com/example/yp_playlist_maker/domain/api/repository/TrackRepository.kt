package com.example.yp_playlist_maker.domain.api.repository

import com.example.yp_playlist_maker.domain.models.Track
import com.example.yp_playlist_maker.util.Resource

interface TrackRepository {
    fun searchTrack(expression: String): Resource<List<Track>>
}