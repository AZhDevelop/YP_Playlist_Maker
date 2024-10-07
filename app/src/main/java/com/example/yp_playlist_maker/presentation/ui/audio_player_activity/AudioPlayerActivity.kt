package com.example.yp_playlist_maker.presentation.ui.audio_player_activity

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.creator.Creator
import com.example.yp_playlist_maker.domain.api.interactor.PlayTrackInteractor
import com.example.yp_playlist_maker.domain.models.PlayerParams
import com.example.yp_playlist_maker.domain.models.Track
import com.example.yp_playlist_maker.presentation.converter.Converter
import com.example.yp_playlist_maker.presentation.ui.application.gone

class AudioPlayerActivity : AppCompatActivity() {

    private var url: String = EMPTY_STRING
    private lateinit var play: Button
    private lateinit var timer: TextView
    private lateinit var playTrack: PlayTrackInteractor
    private lateinit var playerParams: PlayerParams

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audioplayer)

        val backButton = findViewById<ImageView>(R.id.iw_back)
        val trackImage = findViewById<ImageView>(R.id.track_image)
        val trackNameTextView = findViewById<TextView>(R.id.track_name)
        val artistNameTextView = findViewById<TextView>(R.id.artist_name)
        val trackDuration = findViewById<TextView>(R.id.duration_value)
        val trackAlbum = findViewById<TextView>(R.id.album_text)
        val trackAlbumValue = findViewById<TextView>(R.id.album_value)
        val trackYear = findViewById<TextView>(R.id.year_value)
        val trackGenre = findViewById<TextView>(R.id.genre_value)
        val trackCountry = findViewById<TextView>(R.id.country_value)
        val getTrackExtra = IntentCompat.getParcelableExtra(intent, INTENT_PUTTED_TRACK, Track::class.java)
        val trackAlbumIntent = getTrackExtra?.collectionName

        val onPrepare: () -> Unit = {
            playerParams.play.isEnabled = true
            playerParams.play.alpha = ALPHA_100
        }
        val onComplete: () -> Unit = {
            playerParams.play.setBackgroundResource(R.drawable.btn_play)
            playerParams.timer.text = DEFAULT_TIME
        }
        val onStart: () -> Unit = {
            playerParams.play.setBackgroundResource(R.drawable.btn_pause)
        }

        url = getTrackExtra?.previewUrl.toString()
        play = findViewById(R.id.play)
        play.alpha = ALPHA_25
        timer = findViewById(R.id.play_time)

        playerParams = PlayerParams(url, play, timer)
        playTrack = Creator.providePlayTrackInteractor(playerParams)



        playTrack.preparePlayer(url, onPrepare, onComplete)
        play.setOnClickListener { playTrack.playbackControl(onStart) }

        Glide.with(this)
            .load(Converter.convertUrl(getTrackExtra?.artworkUrl100.toString()))
            .centerCrop()
            .transform(RoundedCorners(Converter.dpToPx(PLAYER_IMAGE_RADIUS)))
            .placeholder(R.drawable.img_placeholder_audio_player)
            .into(trackImage)

        trackNameTextView.text = getTrackExtra?.trackName
        artistNameTextView.text = getTrackExtra?.artistName
        trackDuration.text = Converter.convertMillis((getTrackExtra?.trackTimeMillis.toString()))

        if (trackAlbumIntent.isNullOrEmpty()) {
            trackAlbum.gone()
            trackAlbumValue.gone()
        } else {
            trackAlbumValue.text = trackAlbumIntent
        }

        trackYear.text = getTrackExtra?.releaseDate
            .toString()
            .replaceAfter("-","")
            .replace("-", "")
        trackGenre.text = getTrackExtra?.primaryGenreName
        trackCountry.text = getTrackExtra?.country

        backButton.setOnClickListener {
            finish()
        }
    }

    override fun onStop() {
        super.onStop()
        playTrack.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        playTrack.releasePlayer()
        playTrack.threadRemoveCallbacks()
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