package com.example.yp_playlist_maker.database.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.yp_playlist_maker.database.data.entity.TracksInPlaylistsEntity

@Dao
interface TracksInPlaylistsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrackToPlaylist(tracksInPlaylistsEntity: TracksInPlaylistsEntity)

    @Query("SELECT playlistId FROM tracks_in_playlists_table WHERE trackId = :trackId")
    suspend fun checkTrackInPlaylist(trackId: String): List<Int>

    @Query("SELECT * FROM tracks_in_playlists_table WHERE playlistId = :playlistId")
    suspend fun getTracksFromPlaylist(playlistId: Int): List<TracksInPlaylistsEntity>

    @Delete
    suspend fun deleteTrackFromPlaylist(tracksInPlaylistsEntity: TracksInPlaylistsEntity)

    @Query("SELECT elementId FROM tracks_in_playlists_table WHERE playlistId = :playlistId AND trackId = :trackId")
    suspend fun getElementId(playlistId: Int, trackId: String): Int

}