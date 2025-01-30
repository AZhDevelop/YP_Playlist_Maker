package com.example.yp_playlist_maker.database.domain.models

data class TracksInPlaylists(
    val elementId: Int,
    val playlistId: Int,
    val trackId: String,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: String,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String
)
