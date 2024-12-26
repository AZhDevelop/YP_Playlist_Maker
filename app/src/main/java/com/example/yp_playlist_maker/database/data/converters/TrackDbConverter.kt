package com.example.yp_playlist_maker.database.data.converters

import com.example.yp_playlist_maker.database.data.entity.TrackEntity
import com.example.yp_playlist_maker.search.domain.models.Track

class TrackDbConverter {

    fun map(trackEntity: TrackEntity): Track {
        return Track(
            trackEntity.trackName,
            trackEntity.artistName,
            trackEntity.trackTimeMillis,
            trackEntity.artworkUrl100,
            trackEntity.collectionName,
            trackEntity.releaseDate,
            trackEntity.primaryGenreName,
            trackEntity.country,
            trackEntity.previewUrl
        )
    }

}