package com.example.yp_playlist_maker.database.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.yp_playlist_maker.database.data.entity.TrackEntity

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Delete
    suspend fun deleteTrack(track: TrackEntity)

    @Query("SELECT * FROM track_table")
    suspend fun getTrackList(): List<TrackEntity>

    @Query("SELECT isFavourite FROM track_table WHERE trackId == :trackId")
    suspend fun checkIsTrackFavourite(trackId: String): Boolean

}