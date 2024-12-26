package com.example.yp_playlist_maker.util

import android.content.res.Resources
import android.util.DisplayMetrics
import com.example.yp_playlist_maker.database.data.entity.TrackEntity
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
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(time.toInt()).toString()
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

            trackEntity.isFavourite
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

            track.isFavourite
        )
    }

}