package com.example.yp_playlist_maker.util

import android.content.res.Resources
import android.util.DisplayMetrics
import com.example.yp_playlist_maker.database.data.entity.PlaylistEntity
import com.example.yp_playlist_maker.database.data.entity.TrackEntity
import com.example.yp_playlist_maker.database.data.entity.TracksInPlaylistsEntity
import com.example.yp_playlist_maker.database.domain.models.Playlist
import com.example.yp_playlist_maker.database.domain.models.TracksInPlaylists
import com.example.yp_playlist_maker.search.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.roundToInt

object Converter {

    fun dpToPx(value : Int): Int {
        val displayMetrics = Resources.getSystem().displayMetrics
        return (value * (displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT.toFloat())).roundToInt()
    }

    fun convertMillis(time: String): String {
        return try{
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(time.toInt()).toString()
        } catch (e: Exception) {
            time
        }
    }

    fun convertUrl(url: String): String {
        return url.replaceAfterLast('/',"512x512bb.jpg")
    }

    fun convertTrackEntityToTrack(trackEntity: TrackEntity): Track {
        return Track(
            trackEntity.trackId,
            trackEntity.trackName,
            trackEntity.artistName,
            trackEntity.trackTimeMillis,
            trackEntity.artworkUrl100,
            trackEntity.collectionName,
            trackEntity.releaseDate,
            trackEntity.primaryGenreName,
            trackEntity.country,
            trackEntity.previewUrl,
            trackEntity.isFavourite,
            trackEntity.playlistName
        )
    }

    fun convertTrackToTrackEntity(track: Track): TrackEntity {
        return TrackEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            track.isFavourite,
            track.playlistName
        )
    }

    fun convertPlaylistEntityToPlaylist(playlistEntity: PlaylistEntity): Playlist {
        return Playlist(
            playlistEntity.playlistId,
            playlistEntity.playlistName,
            playlistEntity.playlistDescription,
            playlistEntity.playlistCoverPath,
            playlistEntity.playlistSize
        )
    }

    fun convertPlaylistToPlaylistEntity(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlist.playlistId,
            playlist.playlistName,
            playlist.playlistDescription,
            playlist.playlistCoverPath,
            playlist.playlistSize
        )
    }

    fun convertToTracksInPlaylist(tracksInPlaylistsEntity: TracksInPlaylistsEntity) : TracksInPlaylists {
        return TracksInPlaylists(
            tracksInPlaylistsEntity.elementId,
            tracksInPlaylistsEntity.playlistId,
            tracksInPlaylistsEntity.trackId,
            tracksInPlaylistsEntity.trackName,
            tracksInPlaylistsEntity.artistName,
            tracksInPlaylistsEntity.trackTimeMillis,
            tracksInPlaylistsEntity.artworkUrl100,
            tracksInPlaylistsEntity.collectionName,
            tracksInPlaylistsEntity.releaseDate,
            tracksInPlaylistsEntity.primaryGenreName,
            tracksInPlaylistsEntity.country,
            tracksInPlaylistsEntity.previewUrl
        )
    }

    fun convertToTracksInPlaylistsEntity(tracksInPlaylists: TracksInPlaylists) : TracksInPlaylistsEntity {
        return TracksInPlaylistsEntity(
            tracksInPlaylists.elementId,
            tracksInPlaylists.playlistId,
            tracksInPlaylists.trackId,
            tracksInPlaylists.trackName,
            tracksInPlaylists.artistName,
            tracksInPlaylists.trackTimeMillis,
            tracksInPlaylists.artworkUrl100,
            tracksInPlaylists.collectionName,
            tracksInPlaylists.releaseDate,
            tracksInPlaylists.primaryGenreName,
            tracksInPlaylists.country,
            tracksInPlaylists.previewUrl
        )
    }

}