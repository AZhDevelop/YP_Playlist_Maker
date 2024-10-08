package com.example.yp_playlist_maker.domain.api.interactor

import com.example.yp_playlist_maker.domain.models.Track

interface TrackInteractor {

    fun searchTrack(expression: String, consumer: TrackConsumer)

    interface TrackConsumer {
        fun consume(foundTrack: List<Track>?, errorMessage: String?)
    }

}