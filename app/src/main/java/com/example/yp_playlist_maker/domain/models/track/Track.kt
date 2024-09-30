package com.example.yp_playlist_maker.domain.models.track

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Track(
    var trackName: String,
    var artistName: String,
    val trackTimeMillis: String,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String
): Parcelable