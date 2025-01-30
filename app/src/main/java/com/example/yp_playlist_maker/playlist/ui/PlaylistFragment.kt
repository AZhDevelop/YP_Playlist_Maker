package com.example.yp_playlist_maker.playlist.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
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
import com.example.yp_playlist_maker.search.ui.TrackAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
    private var sharedMessage: String = ""

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
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        val playlist = playlistArgs.playlist
        viewmodel.setPlaylistData(playlist)

        setUpPlaylistObservers()
        setRecyclerView()
        viewmodel.setTrackListMessage(playlist)
        viewmodel.setTracksInPlaylist(playlist.playlistId)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        adapter.onTrackClick = {
            val action = PlaylistFragmentDirections.actionPlaylistFragmentToAudioPlayerFragment(it)
            findNavController().navigate(action)
        }

        adapter.onLongTrackClick = {
            showDeleteTrackDialog(it, playlist)
        }

        binding.icShare.setOnClickListener {
            if (sharedMessage == "0") {
                Toast.makeText(requireContext(), getString(R.string.share_error), Toast.LENGTH_SHORT).show()
            } else {
                Log.d("log", sharedMessage)
            }
        }

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
            handleTracksInPlaylist(tracksInPlaylist)
        }

        viewmodel.getSharedMessage().observe(viewLifecycleOwner) { message ->
            sharedMessage = message
        }
    }

    private fun handleTracksInPlaylist(tracksInPlaylist: List<Track>) {
        adapter.data = tracksInPlaylist
        adapter.notifyDataSetChanged()
        if (tracksInPlaylist.isNotEmpty()) {
            binding.tvBottomSheet.gone()
            binding.rvBottomSheet.visible()
        } else {
            binding.tvBottomSheet.visible()
            binding.rvBottomSheet.gone()
        }
    }

    private fun setRecyclerView() {
        _adapter = TrackAdapter()
        binding.rvBottomSheet.layoutManager = LinearLayoutManager(requireContext())
        binding.rvBottomSheet.adapter = _adapter
    }

    private fun showDeleteTrackDialog(track: Track, playlist: Playlist) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.delete_dialog_title))
            .setMessage(getString(R.string.delete_dialog_message))
            .setNegativeButton(getString(R.string.delete_dialog_negative_button)) { _, _ -> }
            .setPositiveButton(getString(R.string.delete_dialog_positive_button)) { _, _ ->
                viewmodel.deleteTrackFromPlaylist(track, playlist)
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _bottomSheetBehavior = null
        _bottomSheetContainer = null
    }
}