package com.example.yp_playlist_maker.creator

import com.example.yp_playlist_maker.player.data.impl.PlayTrackRepositoryImpl
import com.example.yp_playlist_maker.player.domain.api.PlayTrackInteractor
import com.example.yp_playlist_maker.player.domain.api.PlayTrackRepository
import com.example.yp_playlist_maker.player.domain.impl.PlayTrackInteractorImpl

object Creator {

    // Взаимодействие с плеером
    private fun getPlayTrackRepository() : PlayTrackRepository {
        return PlayTrackRepositoryImpl()
    }

    fun providePlayTrackInteractor() : PlayTrackInteractor {
        return PlayTrackInteractorImpl(getPlayTrackRepository())
    }

}