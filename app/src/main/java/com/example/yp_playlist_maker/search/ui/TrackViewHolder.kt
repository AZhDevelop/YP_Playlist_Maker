package com.example.yp_playlist_maker.search.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.yp_playlist_maker.util.Converter
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.search.domain.models.Track

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val trackName: TextView = itemView.findViewById(R.id.track_name)
    private val artistName: TextView = itemView.findViewById(R.id.track_artist)
    private val trackTimeMillis: TextView = itemView.findViewById(R.id.track_length)
    private val artworkUrl100: ImageView = itemView.findViewById(R.id.track_image)
    private var isFavourite: Boolean = false

    fun bind(item: Track) {

        trackName.text = item.trackName
        artistName.text = item.artistName
        isFavourite = item.isFavourite
        trackTimeMillis.text = Converter.convertMillis(item.trackTimeMillis)

        Glide.with(itemView)
            .load(item.artworkUrl100)
            .centerCrop()
            .transform(RoundedCorners(Converter.dpToPx(TRACK_IMAGE_RADIUS)))
            .placeholder(R.drawable.image_placeholder)
            .into(artworkUrl100)

        artistName.requestLayout()
    }

    companion object {
        const val TRACK_IMAGE_RADIUS = 2
    }
}