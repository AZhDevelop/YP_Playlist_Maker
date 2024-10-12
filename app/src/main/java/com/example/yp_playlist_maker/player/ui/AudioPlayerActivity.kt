package com.example.yp_playlist_maker.player.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.creator.Creator
import com.example.yp_playlist_maker.databinding.ActivityAudioplayerBinding
import com.example.yp_playlist_maker.player.domain.api.PlayTrackInteractor
import com.example.yp_playlist_maker.search.domain.models.Track
import com.example.yp_playlist_maker.settings.ui.gone
import com.example.yp_playlist_maker.util.Converter

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioplayerBinding
    private var url: String = EMPTY_STRING
    private lateinit var playTrack: PlayTrackInteractor
    private lateinit var onPause: () -> Unit
    private lateinit var onTimeUpdate: (String) -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioplayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val getTrackExtra =
            IntentCompat.getParcelableExtra(intent, INTENT_PUTTED_TRACK, Track::class.java)
        val trackAlbumIntent = getTrackExtra?.collectionName

        val onPrepare: () -> Unit = {
            binding.play.isEnabled = true
            binding.play.alpha = ALPHA_100
        }
        val onComplete: () -> Unit = {
            binding.play.setBackgroundResource(R.drawable.btn_play)
            binding.playTime.text = DEFAULT_TIME
        }
        val onStart: () -> Unit = {
            binding.play.setBackgroundResource(R.drawable.btn_pause)
        }
        onPause = {
            binding.play.setBackgroundResource(R.drawable.btn_play)
        }
        onTimeUpdate = { time ->
            binding.playTime.text = time
        }

        url = getTrackExtra?.previewUrl.toString()
        binding.play.alpha = ALPHA_25

        playTrack = Creator.providePlayTrackInteractor()

        playTrack.preparePlayer(url, onPrepare, onComplete, onTimeUpdate)
        binding.play.setOnClickListener {
            playTrack.playbackControl(onStart, onPause, onTimeUpdate)
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
        super.onStop()
        playTrack.pausePlayer(onPause)
    }

    override fun onDestroy() {
        super.onDestroy()
        playTrack.releasePlayer()
        playTrack.threadRemoveCallbacks(onTimeUpdate)
    }

    companion object {
        const val PLAYER_IMAGE_RADIUS: Int = 8
        const val INTENT_PUTTED_TRACK: String = "PuttedTrack"
        private const val EMPTY_STRING = ""
        private const val ALPHA_25 = 0.25F
        private const val DEFAULT_TIME = "00:00"
        private const val ALPHA_100 = 1F
    }
}