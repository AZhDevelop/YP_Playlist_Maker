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
import com.example.yp_playlist_maker.settings.ui.gone
import com.example.yp_playlist_maker.util.Converter

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var viewModel: AudioPlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAudioplayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val getTrackExtra = IntentCompat.getParcelableExtra(intent, INTENT_PUTTED_TRACK, Track::class.java)
        val trackAlbumIntent = getTrackExtra?.collectionName
        val url = getTrackExtra?.previewUrl.toString()
        binding.play.alpha = ALPHA_25

        viewModel = ViewModelProvider(this, AudioPlayerViewModelFactory())[AudioPlayerViewModel::class.java]

        viewModel.getPlayButtonEnabled().observe(this) { playButtonEnabled ->
            when (playButtonEnabled) {
                true -> binding.play.isEnabled = true
                false -> binding.play.isEnabled = false
            }
        }

        viewModel.getPlayButtonAlpha().observe(this) { playButtonAlpha ->
            when (playButtonAlpha) {
                ALPHA_100 -> binding.play.alpha = ALPHA_100
            }
        }

        viewModel.getIsPlaying().observe(this) { isPlaying ->
            when (isPlaying) {
                true -> binding.play.setBackgroundResource(R.drawable.btn_pause)
                false -> binding.play.setBackgroundResource(R.drawable.btn_play)
            }
        }

        viewModel.getCurrentTime().observe(this) { currentTime ->
            binding.playTime.text = currentTime
        }

        viewModel.preparePlayer(url)

        binding.play.setOnClickListener {
            viewModel.playbackControl()
        }

        Glide.with(this)
            .load(Converter.convertUrl(getTrackExtra?.artworkUrl100.toString()))
            .centerCrop()
            .transform(RoundedCorners(Converter.dpToPx(PLAYER_IMAGE_RADIUS)))
            .placeholder(R.drawable.img_placeholder_audio_player)
            .into(binding.trackImage)

        binding.trackName.text = getTrackExtra?.trackName
        binding.artistName.text = getTrackExtra?.artistName
        binding.durationValue.text = Converter.convertMillis((getTrackExtra?.trackTimeMillis.toString()))

        if (trackAlbumIntent.isNullOrEmpty()) {
            binding.albumText.gone()
            binding.albumValue.gone()
        } else {
            binding.albumValue.text = trackAlbumIntent
        }

        binding.yearValue.text = getTrackExtra?.releaseDate
            .toString()
            .replaceAfter("-", "")
            .replace("-", "")
        binding.genreValue.text = getTrackExtra?.primaryGenreName
        binding.countryValue.text = getTrackExtra?.country

        binding.iwBack.setOnClickListener {
            finish()
        }
    }

    override fun onStop() {
        viewModel.pausePlayer()
        super.onStop()
    }

    companion object {
        const val PLAYER_IMAGE_RADIUS: Int = 8
        const val INTENT_PUTTED_TRACK: String = "PuttedTrack"
        private const val ALPHA_25 = 0.25F
        private const val ALPHA_100 = 1F
    }
}