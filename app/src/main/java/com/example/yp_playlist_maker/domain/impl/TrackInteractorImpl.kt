package com.example.yp_playlist_maker.domain.impl

import com.example.yp_playlist_maker.domain.api.interactor.TrackInteractor
import com.example.yp_playlist_maker.domain.api.repository.TrackRepository
import java.util.concurrent.Executors

class TrackInteractorImpl(private val repository: TrackRepository) : TrackInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTrack(expression: String, consumer: TrackInteractor.TrackConsumer) {
        executor.execute {
            try {
                val response = repository.searchTrack(expression)
                if (response.isEmpty()) {
                    consumer.consume(emptyList())
                } else {
                    consumer.consume(response)
                }
            } catch (e: Exception) {
                consumer.error("Connection Error")
            }

        }
    }

}