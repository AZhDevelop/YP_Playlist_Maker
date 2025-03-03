package com.example.yp_playlist_maker.media.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.app.gone
import com.example.yp_playlist_maker.app.visible
import com.example.yp_playlist_maker.databinding.FragmentMediaFavouritesBinding
import com.example.yp_playlist_maker.media.ui.MediaFragmentDirections
import com.example.yp_playlist_maker.media.ui.view_model.MediaFavouritesFragmentViewModel
import com.example.yp_playlist_maker.player.ui.AudioPlayerFragment
import com.example.yp_playlist_maker.search.domain.models.Track
import com.example.yp_playlist_maker.search.ui.TrackAdapter
import com.example.yp_playlist_maker.util.State
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaFavouritesFragment: Fragment() {

    private var _binding: FragmentMediaFavouritesBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<MediaFavouritesFragmentViewModel>()
    private var _adapter: TrackAdapter? = null
    private val adapter get() = _adapter!!
    private var isClickAllowed = true
    private var clickDebounceJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMediaFavouritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            tvPlaceholder.text = getString(R.string.empty_media)
            imgPlaceholder.setImageResource(R.drawable.img_search_error)
        }

        setRecyclerView()
        setFavouritesFragmentObservers()
        viewModel.checkFavouriteTrackList()

        adapter.onTrackClick = {
            if (clickDebounce()) {
                val action = MediaFragmentDirections.actionMediaFragmentToAudioPlayerFragment(it)
                findNavController().navigate(action)
            }
        }

    }

    private fun setFavouritesFragmentObservers() {
        viewModel.getFavouriteTracksList().observe(viewLifecycleOwner) { favouriteTracksList ->
            handleFavouriteTrackList(favouriteTracksList)
        }
        viewModel.getFragmentState().observe(viewLifecycleOwner) { fragmentState ->
            handleFragmentState(fragmentState)
        }
    }

    private fun handleFavouriteTrackList(favouriteTracksList: List<Track>) {
        adapter.data = favouriteTracksList
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
            rvTrack.gone()
            tvPlaceholder.visible()
            imgPlaceholder.visible()
        }
    }

    private fun showRecyclerView() {
        binding.apply {
            rvTrack.visible()
            tvPlaceholder.gone()
            imgPlaceholder.gone()
        }
    }

    private fun setRecyclerView() {
        _adapter = TrackAdapter()
        binding.rvTrack.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTrack.adapter = adapter
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            clickDebounceJob?.cancel()
            clickDebounceJob = lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkFavouriteTrackList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        adapter.onTrackClick = null
        _adapter = null
    }

    companion object {
        fun newInstance() = MediaFavouritesFragment()
        private const val INTENT_PUTTED_TRACK: String = "PuttedTrack"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

}