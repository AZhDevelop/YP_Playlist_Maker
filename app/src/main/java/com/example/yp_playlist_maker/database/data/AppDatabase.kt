package com.example.yp_playlist_maker.database.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.yp_playlist_maker.database.data.dao.PlaylistDao
import com.example.yp_playlist_maker.database.data.dao.TrackDao
import com.example.yp_playlist_maker.database.data.dao.TracksInPlaylistsDao
import com.example.yp_playlist_maker.database.data.entity.PlaylistEntity
import com.example.yp_playlist_maker.database.data.entity.TrackEntity
import com.example.yp_playlist_maker.database.data.entity.TracksInPlaylistsEntity

@Database(version = 1, entities = [TrackEntity::class, PlaylistEntity::class, TracksInPlaylistsEntity::class])
abstract class AppDatabase: RoomDatabase() {

    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun tracksInPlaylistsDao(): TracksInPlaylistsDao

}