package com.example.yp_playlist_maker.database.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Playlist(
    val playlistId: Int,
    val playlistName: String,
    val playlistDescription: String,
    val playlistCoverPath: String,
    val playlistSize: String,
    val playlistDuration: String
): Parcelable
