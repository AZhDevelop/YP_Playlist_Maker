package com.example.yp_playlist_maker.database.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.yp_playlist_maker.database.data.entity.PlaylistEntity
import com.example.yp_playlist_maker.database.data.entity.TracksInPlaylists

@Dao
interface TracksInPlaylistsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrackToPlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM tracks_in_playlists_table WHERE playlistId = :playlistId")
    suspend fun getTracksFromPlaylist(playlistId: Int): List<TracksInPlaylists>

}