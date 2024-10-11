package com.example.yp_playlist_maker.search.data.dto

data class TrackDto(
    var trackName: String,
    var artistName: String,
    val trackTimeMillis: String,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String
)