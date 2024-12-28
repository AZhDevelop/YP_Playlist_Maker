package com.example.yp_playlist_maker.media.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.search.domain.models.Track

class MediaTrackAdapter : RecyclerView.Adapter<MediaTrackViewHolder>() {

    var onTrackClick: ((Track) -> Unit)? = null
    var data: List<Track> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaTrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return MediaTrackViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MediaTrackViewHolder, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener {
            onTrackClick?.invoke(data[position])
        }
    }
}