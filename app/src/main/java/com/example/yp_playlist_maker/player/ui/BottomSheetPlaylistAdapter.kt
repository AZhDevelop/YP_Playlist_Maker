package com.example.yp_playlist_maker.player.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.database.domain.models.Playlist

class BottomSheetPlaylistAdapter: RecyclerView.Adapter<BottomSheetPlaylistViewHolder>() {

    var onPlaylistClick: ((Playlist) -> Unit)? = null
    var data: List<Playlist> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetPlaylistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bottom_sheet_playlist_view, parent, false)
        return BottomSheetPlaylistViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: BottomSheetPlaylistViewHolder, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener {
            onPlaylistClick?.invoke(data[position])
        }
    }

}