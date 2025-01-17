package com.example.yp_playlist_maker.media.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.app.gone
import com.example.yp_playlist_maker.app.visible
import com.example.yp_playlist_maker.database.domain.models.Playlist
import com.example.yp_playlist_maker.databinding.FragmentMediaPlaylistsBinding
import com.example.yp_playlist_maker.media.ui.PlaylistAdapter
import com.example.yp_playlist_maker.media.ui.view_model.MediaPlaylistsFragmentViewModel
import com.example.yp_playlist_maker.search.ui.TrackAdapter
import com.example.yp_playlist_maker.util.State
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaPlaylistsFragment : Fragment() {

    private var _binding: FragmentMediaPlaylistsBinding? = null
    private val binding get() = _binding!!
    private var _adapter: PlaylistAdapter? = null
    private val adapter get() = _adapter!!
    private val viewModel by viewModel<MediaPlaylistsFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMediaPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            tvPlaceholder.text = getString(R.string.no_playlists)
            imgPlaceholder.setImageResource(R.drawable.img_search_error)
        }

        setRecyclerView()
        setMediaPlaylistsFragmentObservers()
        viewModel.checkPlaylistList()

        binding.btnPlaceholder.setOnClickListener {
            findNavController().navigate(R.id.action_mediaFragment_to_playlistFragment)
        }

    }

    private fun setRecyclerView() {
        _adapter = PlaylistAdapter()
        binding.rvPlaylists.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvPlaylists.adapter = adapter
    }

    private fun setMediaPlaylistsFragmentObservers() {
        viewModel.getPlaylistList().observe(viewLifecycleOwner) { playlistsList ->
            handlePlaylistsList(playlistsList)
        }
        viewModel.getFragmentState().observe(viewLifecycleOwner) { fragmentState ->
            handleFragmentState(fragmentState)
        }

    }

    private fun handlePlaylistsList(playlistsList: List<Playlist>) {
        adapter.data = playlistsList
        adapter.notifyDataSetChanged()
    }

    private fun handleFragmentState(fragmentState: State.FragmentState) {
        when (fragmentState) {
            State.FragmentState.ERROR -> {
                showEmptyMediaError()
            }
            State.FragmentState.SUCCESS -> {
                showRecyclerView()
            }
        }
    }

    private fun showEmptyMediaError() {
        binding.apply {
            rvPlaylists.gone()
            tvPlaceholder.visible()
            imgPlaceholder.visible()
        }
    }

    private fun showRecyclerView() {
        binding.apply {
            rvPlaylists.visible()
            tvPlaceholder.gone()
            imgPlaceholder.gone()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _adapter = null
    }

    companion object {
        fun newInstance() = MediaPlaylistsFragment()
    }

}