package com.example.yp_playlist_maker.media.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.app.gone
import com.example.yp_playlist_maker.app.visible
import com.example.yp_playlist_maker.databinding.ActivityMediaFavouritesFragmentBinding
import com.example.yp_playlist_maker.media.ui.MediaTrackAdapter
import com.example.yp_playlist_maker.media.ui.view_model.FavouritesFragmentViewModel
import com.example.yp_playlist_maker.search.domain.models.Track
import com.example.yp_playlist_maker.util.State
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouritesFragment: Fragment() {

    private lateinit var binding: ActivityMediaFavouritesFragmentBinding
    private val viewModel by viewModel<FavouritesFragmentViewModel>()
    private val adapter = MediaTrackAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityMediaFavouritesFragmentBinding.inflate(inflater, container, false)
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
        binding.rvTrack.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTrack.adapter = adapter
    }

    companion object {
        fun newInstance() = FavouritesFragment()
    }

}