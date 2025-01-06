package com.example.yp_playlist_maker.database.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.yp_playlist_maker.database.data.dao.TrackDao
import com.example.yp_playlist_maker.database.data.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class])
abstract class AppDatabase: RoomDatabase() {

    abstract fun trackDao(): TrackDao

}