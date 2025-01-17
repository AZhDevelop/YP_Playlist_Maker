package com.example.yp_playlist_maker.player.ui

import android.animation.ArgbEvaluator
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.app.gone
import com.example.yp_playlist_maker.databinding.FragmentAudioplayerBinding
import com.example.yp_playlist_maker.player.ui.view_model.AudioPlayerViewModel
import com.example.yp_playlist_maker.search.domain.models.Track
import com.example.yp_playlist_maker.util.State
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AudioPlayerFragment : Fragment() {

    private val trackArgs by navArgs<AudioPlayerFragmentArgs>()
    private val viewModel by viewModel<AudioPlayerViewModel>()
    private var _binding: FragmentAudioplayerBinding? = null
    private val binding get() = _binding!!
    private var isTrackFavourite: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAudioplayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val track = trackArgs.track
        viewModel.setTrackData(track)

        val bottomSheetContainer = binding.bottomSheet
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        viewModel.preparePlayer()

        setupPlayerObservers()

        binding.play.setOnClickListener { viewModel.playbackControl() }

        binding.like.setOnClickListener {
            saveDeleteFavourites(isTrackFavourite)
        }

        binding.addToPlaylist.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {

            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                val startColor = R.color.main_background
                val endColor = TRANSPARENT_BACKGROUND
                val normalizedOffset = (slideOffset + 1).coerceIn(0f, 1f)
                val blendedColor = ArgbEvaluator().evaluate(normalizedOffset, startColor, endColor) as Int
                binding.audioplayerActivity.setBackgroundColor(blendedColor)
            }
        })

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.pausePlayer()
    }

    private fun fillTrackData(track: Track) {
        binding.apply {
            trackName.text = track.trackName
            artistName.text = track.artistName
            durationValue.text = track.trackTimeMillis
            if (track.collectionName.isEmpty()) {
                albumText.gone()
                albumValue.gone()
            } else {
                albumValue.text = track.collectionName
            }
            yearValue.text = track.releaseDate
            genreValue.text = track.primaryGenreName
            countryValue.text = track.country
            loadTrackImage(track.artworkUrl100)
        }
    }

    private fun loadTrackImage(artworkUrl100: String) {
        Glide.with(this)
            .load(artworkUrl100)
            .centerCrop()
            .transform(RoundedCorners(viewModel.getRoundedCorners(PLAYER_IMAGE_RADIUS)))
            .placeholder(R.drawable.img_placeholder_audio_player)
            .into(binding.trackImage)
    }

    private fun setupPlayerObservers() {
        viewModel.getAudioPlayerStatus().observe(viewLifecycleOwner) { status ->
            handlePlayerStatus(status)
        }

        viewModel.getCurrentTime().observe(viewLifecycleOwner) { currentTime ->
            binding.playTime.text = currentTime
        }

        viewModel.getTrackData().observe(viewLifecycleOwner) { trackData ->
            fillTrackData(trackData)
        }

        viewModel.getIsFavourite().observe(viewLifecycleOwner) { isFavourite ->
            handleIsFavourite(isFavourite)
        }
    }

    private fun handlePlayerStatus(status: State.PlayerState) {
        when (status) {
            State.PlayerState.LOADING -> binding.play.alpha = ALPHA_25
            State.PlayerState.PREPARED -> {
                binding.play.isEnabled = true
                binding.play.alpha = ALPHA_100
            }
            State.PlayerState.COMPLETED -> {
                binding.play.setBackgroundResource(R.drawable.btn_play)
            }
            State.PlayerState.START -> binding.play.setBackgroundResource(R.drawable.btn_pause)
            State.PlayerState.PAUSE -> binding.play.setBackgroundResource(R.drawable.btn_play)
        }
    }

    private fun handleIsFavourite(isFavourite: Boolean) {
        if (isFavourite) {
            binding.like.setBackgroundResource(R.drawable.btn_like_active)
            isTrackFavourite = true
        } else {
            binding.like.setBackgroundResource(R.drawable.btn_like_non_active)
            isTrackFavourite = false
        }
    }

    private fun saveDeleteFavourites(isFavourite: Boolean) {
        if (isFavourite) {
            viewModel.deleteTrackFromFavourites()
        } else {
            viewModel.saveTrackToFavourites()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val PLAYER_IMAGE_RADIUS: Int = 8
        private const val ALPHA_25 = 0.25F
        private const val ALPHA_100 = 1F
        private val TRANSPARENT_BACKGROUND = Color.argb(128, 26, 27, 34)
    }

}