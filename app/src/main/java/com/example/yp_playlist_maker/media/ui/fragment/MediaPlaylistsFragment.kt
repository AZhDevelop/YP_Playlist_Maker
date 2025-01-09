package com.example.yp_playlist_maker.media.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.databinding.FragmentMediaPlaylistsBinding
import com.example.yp_playlist_maker.media.ui.view_model.PlaylistsFragmentViewModel
import com.example.yp_playlist_maker.util.State
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaPlaylistsFragment : Fragment() {

    private var _binding: FragmentMediaPlaylistsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<PlaylistsFragmentViewModel>()

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
        viewModel.getFragmentState().observe(viewLifecycleOwner) { fragmentState ->
            when (fragmentState) {
                State.FragmentState.ERROR -> {
                    showNoPlaylistsError()
                }
                else -> {
                    binding.tvPlaceholder.text = getString(R.string.no_playlists) //Пока как заглушка
                }
            }
        }
    }

    private fun showNoPlaylistsError() {
        binding.apply {
            tvPlaceholder.text = getString(R.string.no_playlists)
            btnPlaceholder.text = getString(R.string.new_playlist)
            imgPlaceholder.setImageResource(R.drawable.img_search_error)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = MediaPlaylistsFragment()
    }

}