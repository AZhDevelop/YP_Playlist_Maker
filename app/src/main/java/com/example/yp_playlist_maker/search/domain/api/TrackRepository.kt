package com.example.yp_playlist_maker.search.domain.api

import com.example.yp_playlist_maker.util.Resource

interface TrackRepository {
    fun searchTrack(expression: String): Resource
}