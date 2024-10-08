package com.example.yp_playlist_maker.domain.api.repository

import com.example.yp_playlist_maker.domain.models.Track

interface TrackRepository {
    fun searchTrack(expression: String): List<Track>
}