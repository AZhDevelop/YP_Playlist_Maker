package com.example.yp_playlist_maker.database.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.yp_playlist_maker.database.data.entity.PlaylistEntity
import com.example.yp_playlist_maker.database.data.entity.TrackEntity

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

}