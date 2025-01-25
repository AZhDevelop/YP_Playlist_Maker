package com.example.yp_playlist_maker.database.domain.models

data class Playlist(
    val playlistId: Int,
    val playlistName: String,
    val playlistDescription: String,
    val playlistCoverPath: String,
    val playlistSize: String
)
