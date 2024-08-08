package com.example.yp_playlist_maker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val trackName: TextView = itemView.findViewById(R.id.track_name)
    private val artistName: TextView = itemView.findViewById(R.id.track_artist)
    private val trackTimeMillis: TextView = itemView.findViewById(R.id.track_length)
    private val artworkUrl100: ImageView = itemView.findViewById(R.id.track_image)
    private val trackImageCornerRadius: Int = 2

    fun bind(item: Track, listener: Listener) {

        trackName.text = item.trackName
        artistName.text = item.artistName
        trackTimeMillis.text = Converter().convertMillis(item.trackTimeMillis)

        Glide.with(itemView)
            .load(item.artworkUrl100)
            .centerCrop()
            .transform(RoundedCorners(Converter().dpToPx(trackImageCornerRadius)))
            .placeholder(R.drawable.image_placeholder)
            .into(artworkUrl100)

        artistName.requestLayout()

        itemView.setOnClickListener {
            listener.onClick(item)
        }
    }

}





