package com.example.yp_playlist_maker.database.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks_in_playlists_table")
class TracksInPlaylistsEntity(
    @PrimaryKey(autoGenerate = true)
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
    val previewUrl: String,
    val isFavourite: Boolean
)