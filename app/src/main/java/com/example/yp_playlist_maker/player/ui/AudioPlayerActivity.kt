package com.example.yp_playlist_maker.player.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.databinding.ActivityAudioplayerBinding
import com.example.yp_playlist_maker.player.ui.view_model.AudioPlayerViewModel
import com.example.yp_playlist_maker.player.ui.view_model.AudioPlayerViewModelFactory
import com.example.yp_playlist_maker.search.domain.models.Track
import com.example.yp_playlist_maker.app.gone
import org.koin.android.ext.android.get

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var viewModel: AudioPlayerViewModel
    private lateinit var binding: ActivityAudioplayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioplayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val getTrackExtra =
            IntentCompat.getParcelableExtra(intent, INTENT_PUTTED_TRACK, Track::class.java)

        viewModel = ViewModelProvider(this, AudioPlayerViewModelFactory(getTrackExtra))[AudioPlayerViewModel::class.java]

        viewModel.setTrackData(getTrackExtra)
        viewModel.preparePlayer()

        setupPlayerObservers()

        binding.play.setOnClickListener { viewModel.playbackControl() }

        binding.iwBack.setOnClickListener { finish() }
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
        viewModel.getAudioPlayerStatus().observe(this) { status ->
            handlePlayerStatus(status)
        }

        viewModel.getCurrentTime().observe(this) { currentTime ->
            binding.playTime.text = currentTime
        }

        viewModel.getTrackData().observe(this) { trackData ->
            fillTrackData(trackData)
        }
    }

    private fun handlePlayerStatus(status: String) {
        when (status) {
            LOADING -> binding.play.alpha = ALPHA_25
            PREPARED -> {
                binding.play.isEnabled = true
                binding.play.alpha = ALPHA_100
            }
            COMPLETED -> {
                binding.play.setBackgroundResource(R.drawable.btn_play)
                binding.playTime.text = DEFAULT_TIME
            }
            START -> binding.play.setBackgroundResource(R.drawable.btn_pause)
            PAUSE -> binding.play.setBackgroundResource(R.drawable.btn_play)
        }
    }

    companion object {
        const val PLAYER_IMAGE_RADIUS: Int = 8
        const val INTENT_PUTTED_TRACK: String = "PuttedTrack"
        private const val ALPHA_25 = 0.25F
        private const val DEFAULT_TIME = "00:00"
        private const val ALPHA_100 = 1F
        private const val LOADING = "Loading"
        private const val PREPARED = "Prepared"
        private const val COMPLETED = "Completed"
        private const val START = "Start"
        private const val PAUSE = "Pause"
    }

}