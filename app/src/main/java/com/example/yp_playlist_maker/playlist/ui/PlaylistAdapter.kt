package com.example.yp_playlist_maker.playlist.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.database.domain.models.TracksInPlaylists
import com.example.yp_playlist_maker.search.domain.models.Track

class PlaylistAdapter: RecyclerView.Adapter<PlaylistViewHolder>() {

//    var onTrackClick: ((Track) -> Unit)? = null
    var data: List<TracksInPlaylists> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(data[position])
//        holder.itemView.setOnClickListener {
//            onTrackClick?.invoke(data[position])
//        }
    }

}