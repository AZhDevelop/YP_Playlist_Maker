package com.example.yp_playlist_maker.media.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.database.domain.models.Playlist
import com.example.yp_playlist_maker.util.Converter

class PlaylistViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val playlistName: TextView = itemView.findViewById(R.id.playlist_name)
    private val playlistSize: TextView = itemView.findViewById(R.id.playlist_size)
    private val playlistCover: ImageView = itemView.findViewById(R.id.playlist_cover)

    fun bind(item: Playlist) {

        val imagePath = item.playlistCoverPath

        playlistName.text = item.playlistName
        playlistSize.text = Converter.convertPlaylistSizeValue(item.playlistSize)

        if (imagePath == "null") {
            Glide.with(itemView)
                .load(R.drawable.image_placeholder)
                .into(playlistCover)
        } else {
            Glide.with(itemView)
                .load(item.playlistCoverPath)
                .centerCrop()
                .transform(RoundedCorners(Converter.dpToPx(PLAYLIST_COVER_RADIUS)))
                .into(playlistCover)
        }
    }

    companion object {
        const val PLAYLIST_COVER_RADIUS = 8
    }

}