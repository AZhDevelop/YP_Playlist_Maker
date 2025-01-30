package com.example.yp_playlist_maker.playlist.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.app.gone
import com.example.yp_playlist_maker.app.visible
import com.example.yp_playlist_maker.database.domain.models.Playlist
import com.example.yp_playlist_maker.databinding.FragmentPlaylistBinding
import com.example.yp_playlist_maker.playlist.ui.view_model.PlaylistFragmentViewModel
import com.example.yp_playlist_maker.search.domain.models.Track
import com.example.yp_playlist_maker.search.ui.SearchFragmentDirections
import com.example.yp_playlist_maker.search.ui.TrackAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment: Fragment() {

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!
    private val viewmodel by viewModel<PlaylistFragmentViewModel>()
    private val playlistArgs by navArgs<PlaylistFragmentArgs>()
    private var _bottomSheetContainer: LinearLayout? = null
    private val bottomSheetContainer get() = _bottomSheetContainer!!
    private var _bottomSheetBehavior: BottomSheetBehavior<LinearLayout>? = null
    private val bottomSheetBehavior get() = _bottomSheetBehavior!!
    private var _adapter: TrackAdapter? = null
    private val adapter get() = _adapter!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _bottomSheetContainer = binding.bottomSheet
        _bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer)

        val playlist = playlistArgs.playlist
        viewmodel.setPlaylistData(playlist)

        setUpPlaylistObservers()
        setRecyclerView()
        viewmodel.setTracksInPlaylist(playlist.playlistId)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

//        adapter.onTrackClick = {
//            val action = PlaylistFragmentDirections.actionPlaylistFragmentToAudioPlayerFragment(it)
//            findNavController().navigate(action)
//        }

    }

    private fun fillPlaylistData(playlist: Playlist) {
        binding.apply {
            tvPlaylistName.text = playlist.playlistName
            tvPlaylistDescription.text = playlist.playlistDescription
            tvPlaylistDuration.text = playlist.playlistDuration
            tvPlaylistTracks.text = playlist.playlistSize
            loadPlaylistCover(playlist.playlistCoverPath)
        }
    }

    private fun loadPlaylistCover(playlistCoverPath: String) {
        if (playlistCoverPath != "null") {
            Glide.with(this)
                .load(playlistCoverPath)
                .centerCrop()
                .placeholder(R.drawable.img_placeholder_audio_player)
                .into(binding.ivPlaylistCover)
        }
    }

    private fun setUpPlaylistObservers() {
        viewmodel.getPlaylistData().observe(viewLifecycleOwner) { playlistData ->
            fillPlaylistData(playlistData)
        }

        viewmodel.getTracksInPlaylist().observe(viewLifecycleOwner) { tracksInPlaylist ->
            Log.d("log", "$tracksInPlaylist")
            handleTracksInPlaylist(tracksInPlaylist)
        }
    }

    private fun handleTracksInPlaylist(tracksInPlaylist: List<Track>) {
        if (tracksInPlaylist.isNotEmpty()) {
            adapter.data = tracksInPlaylist
            adapter.notifyDataSetChanged()
            binding.tvBottomSheet.gone()
        } else {
            binding.tvBottomSheet.visible()
        }
    }

    private fun setRecyclerView() {
        _adapter = TrackAdapter()
        binding.rvBottomSheet.layoutManager = LinearLayoutManager(requireContext())
        binding.rvBottomSheet.adapter = _adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}