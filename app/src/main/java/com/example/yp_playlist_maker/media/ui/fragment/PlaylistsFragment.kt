package com.example.yp_playlist_maker.media.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.yp_playlist_maker.databinding.ActivityMediaPlaylistsFragmentBinding
import com.example.yp_playlist_maker.media.ui.view_model.PlaylistsFragmentViewModel
import com.example.yp_playlist_maker.util.State
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    private lateinit var binding: ActivityMediaPlaylistsFragmentBinding
    private val viewModel by viewModel<PlaylistsFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityMediaPlaylistsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getFragmentState().observe(viewLifecycleOwner) { fragmentState ->
            when (fragmentState) {
                State.FragmentState.ERROR -> {
                    binding.tvPlaceholder.text = NO_PLAYLISTS
                }

                else -> {
                    binding.tvPlaceholder.text = NO_PLAYLISTS //Пока как заглушка
                }
            }
        }
    }

    companion object {
        private const val NO_PLAYLISTS = "Вы не создали ни одного плейлиста"

        fun newInstance() = PlaylistsFragment()
    }

}