package com.example.yp_playlist_maker.database.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.yp_playlist_maker.database.data.entity.PlaylistEntity

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlist_table")
    suspend fun getPlaylistList(): List<PlaylistEntity>

    @Update
    suspend fun updatePlaylist(playlist: PlaylistEntity)

    @Query("SELECT playlistSize FROM playlist_table WHERE playlistId = :playlistId")
    suspend fun getPlaylistSize(playlistId: Int): String

    @Query("SELECT playlistDuration FROM playlist_table WHERE playlistId = :playlistId")
    suspend fun getPlaylistDuration(playlistId: Int): String

    @Delete
    suspend fun deletePlaylist(playlist: PlaylistEntity)
}