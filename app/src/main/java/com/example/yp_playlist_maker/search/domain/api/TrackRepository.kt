package com.example.yp_playlist_maker.search.domain.api

import com.example.yp_playlist_maker.util.Resource
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    fun searchTrack(expression: String): Flow<Resource>
}