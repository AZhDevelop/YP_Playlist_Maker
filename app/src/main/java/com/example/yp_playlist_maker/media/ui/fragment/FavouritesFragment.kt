package com.example.yp_playlist_maker.media.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.yp_playlist_maker.databinding.ActivityMediaFavouritesFragmentBinding
import com.example.yp_playlist_maker.media.ui.view_model.FavouritesFragmentViewModel
import com.example.yp_playlist_maker.util.State
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouritesFragment: Fragment() {

    private lateinit var binding: ActivityMediaFavouritesFragmentBinding
    private val viewModel by viewModel<FavouritesFragmentViewModel>()

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
        viewModel.getFragmentState().observe(viewLifecycleOwner) { fragmentState ->
            when (fragmentState) {
                State.FragmentState.ERROR -> {
                    binding.tvPlaceholder.text = EMPTY_MEDIA
                }

                else -> {
                    binding.tvPlaceholder.text = EMPTY_MEDIA //Пока как заглушка
                }
            }
        }
    }

    companion object {
        private const val EMPTY_MEDIA = "Ваша медиатека пуста"

        fun newInstance() = FavouritesFragment()
    }

}