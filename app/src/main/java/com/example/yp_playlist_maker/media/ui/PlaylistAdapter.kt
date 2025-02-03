package com.example.yp_playlist_maker.media.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.database.domain.models.Playlist
import com.example.yp_playlist_maker.search.domain.models.Track

class PlaylistAdapter: RecyclerView.Adapter<PlaylistViewHolder>() {

    var onPlaylistClick: ((Playlist) -> Unit)? = null
    var data: List<Playlist> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_view, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener {
            onPlaylistClick?.invoke(data[position])
        }
    }
}