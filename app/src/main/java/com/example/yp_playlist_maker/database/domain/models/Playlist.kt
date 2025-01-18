package com.example.yp_playlist_maker.database.domain.models

import androidx.room.PrimaryKey

data class Playlist(
    @PrimaryKey(autoGenerate = true)
    val playlistId: Int,
    val playlistName: String,
    val playlistDescription: String,
    val playlistCoverPath: String,
    var trackIdList: String,
    var playlistSize: String
)
