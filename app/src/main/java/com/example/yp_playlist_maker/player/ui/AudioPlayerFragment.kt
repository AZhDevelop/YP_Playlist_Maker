package com.example.yp_playlist_maker.player.ui

import android.animation.ArgbEvaluator
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.app.gone
import com.example.yp_playlist_maker.app.visible
import com.example.yp_playlist_maker.database.domain.models.Playlist
import com.example.yp_playlist_maker.databinding.FragmentAudioplayerBinding
import com.example.yp_playlist_maker.player.ui.view_model.AudioPlayerViewModel
import com.example.yp_playlist_maker.search.domain.models.Track
import com.example.yp_playlist_maker.util.State
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class AudioPlayerFragment : Fragment() {

    private val viewModel by viewModel<AudioPlayerViewModel>()
    private val trackArgs by navArgs<AudioPlayerFragmentArgs>()
    private var _binding: FragmentAudioplayerBinding? = null
    private val binding get() = _binding!!
    private var _adapter: BottomSheetPlaylistAdapter? = null
    private val adapter get() = _adapter!!
    private var isTrackFavourite: Boolean = false
    private var isClickAllowed = true
    private var clickDebounceJob: Job? = null
    private var playlistName: String = ""
    private var trackName: String = ""
    private var _bottomSheetContainer: LinearLayout? = null
    private val bottomSheetContainer get() = _bottomSheetContainer!!
    private var _bottomSheetBehavior: BottomSheetBehavior<LinearLayout>? = null
    private val bottomSheetBehavior get() = _bottomSheetBehavior!!

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

        if (viewModel.backgroundColor.value == Color.TRANSPARENT) {
            val defaultColor = ContextCompat.getColor(requireContext(), R.color.main_background)
            viewModel.setBackgroundColor(defaultColor)
        }

        val track = trackArgs.track
        viewModel.setTrackData(track)

        _bottomSheetContainer = binding.bottomSheet
        _bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        setRecyclerView()
        setupPlayerObservers()
        viewModel.checkPlaylistList()

        binding.play.setOnClickListener { viewModel.playbackControl() }

        binding.like.setOnClickListener {
            saveDeleteFavourites(isTrackFavourite)
        }

        binding.addToPlaylist.setOnClickListener {
            viewModel.checkPlaylistList()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        binding.btnBottomSheet.setOnClickListener {
            findNavController().navigate(R.id.action_audioPlayerFragment_to_playlistFragment)
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {

            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                val startColor = ContextCompat.getColor(requireContext(), R.color.main_background)
                val endColor = TRANSPARENT_BACKGROUND
                val normalizedOffset = (slideOffset + 1).coerceIn(0f, 1f)
                val blendedColor = ArgbEvaluator().evaluate(normalizedOffset, startColor, endColor) as Int
                binding.audioplayerFragment.setBackgroundColor(blendedColor)
                viewModel.setBackgroundColor(blendedColor)
            }
        })

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        adapter.onPlaylistClick = {
            if (clickDebounce()) {
                trackName = binding.trackName.text.toString()
                playlistName = it.playlistName
                viewModel.addTrackToPlaylist(track, it)
            }
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

        viewModel.isFavourite.observe(viewLifecycleOwner) { isFavourite ->
            handleIsFavourite(isFavourite)
        }

        viewModel.backgroundColor.observe(viewLifecycleOwner) { color ->
            binding.audioplayerFragment.setBackgroundColor(color)
        }

        viewModel.getPlaylistList().observe(viewLifecycleOwner) { playlistsList ->
            handlePlaylistsList(playlistsList)
        }

        viewModel.getBottomSheetState().observe(viewLifecycleOwner) { bottomSheetState ->
            handleFragmentState(bottomSheetState)
        }

        viewModel.getAddToPlaylistState().observe(viewLifecycleOwner) { addToPlaylistState ->
            handleAddToPlaylistState(addToPlaylistState)
        }
    }

    private fun handlePlaylistsList(playlistsList: List<Playlist>) {
        adapter.data = playlistsList
        adapter.notifyDataSetChanged()
    }

    private fun handleFragmentState(bottomSheetState: State.BottomSheetState) {
        when (bottomSheetState) {
            State.BottomSheetState.EMPTY -> {
                binding.rvBottomSheet.gone()
            }

            State.BottomSheetState.SUCCESS -> {
                binding.rvBottomSheet.visible()
            }
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

    private fun handleAddToPlaylistState(playlistState: State.AddToPlaylistState) {
        val toastSuccessMessage = "Добавлено в плейлист \"$playlistName\""
        val toastErrorMessage = "Трек уже добавлен в плейлист \"$playlistName\""
        when (playlistState) {
            State.AddToPlaylistState.SUCCESS -> {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                Toast.makeText(requireContext(), toastSuccessMessage, Toast.LENGTH_SHORT).show()
            }
            State.AddToPlaylistState.ERROR -> {
                Toast.makeText(requireContext(), toastErrorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveDeleteFavourites(isFavourite: Boolean) {
        if (isFavourite) {
            viewModel.deleteTrackFromFavourites()
        } else {
            viewModel.saveTrackToFavourites()
        }
    }

    private fun setRecyclerView() {
        _adapter = BottomSheetPlaylistAdapter()
        binding.rvBottomSheet.layoutManager = LinearLayoutManager(requireContext())
        binding.rvBottomSheet.adapter = adapter
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _adapter = null
        _bottomSheetContainer = null
        _bottomSheetBehavior = null
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val PLAYER_IMAGE_RADIUS: Int = 8
        private const val ALPHA_25 = 0.25F
        private const val ALPHA_100 = 1F
        private val TRANSPARENT_BACKGROUND = Color.argb(128, 26, 27, 34)
    }

}