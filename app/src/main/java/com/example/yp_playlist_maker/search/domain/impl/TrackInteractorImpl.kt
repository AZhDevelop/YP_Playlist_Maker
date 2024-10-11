package com.example.yp_playlist_maker.search.domain.impl

import com.example.yp_playlist_maker.search.domain.api.TrackInteractor
import com.example.yp_playlist_maker.search.domain.api.TrackRepository
import com.example.yp_playlist_maker.util.Resource
import java.util.concurrent.Executors

class TrackInteractorImpl(private val repository: TrackRepository) : TrackInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTrack(expression: String, consumer: TrackInteractor.TrackConsumer) {
        executor.execute {
            when (val resource = repository.searchTrack(expression)) {
                is Resource.Success -> (consumer.consume(resource.data, null))
                is Resource.Error -> (consumer.consume(null, resource.message))
            }
        }
    }

}