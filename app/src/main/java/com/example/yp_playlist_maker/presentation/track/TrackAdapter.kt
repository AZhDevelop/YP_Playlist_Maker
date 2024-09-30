package com.example.yp_playlist_maker.presentation.track

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.domain.models.track.Track

class TrackAdapter : RecyclerView.Adapter<TrackViewHolder>() {

    var onTrackClick: ((Track) -> Unit)? = null
    var data = ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener {
            onTrackClick?.invoke(data[position])
        }
    }
}