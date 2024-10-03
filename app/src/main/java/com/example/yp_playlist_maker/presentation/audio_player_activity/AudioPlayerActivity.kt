package com.example.yp_playlist_maker.presentation.audio_player_activity

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.yp_playlist_maker.domain.use_case.Converter
import com.example.yp_playlist_maker.creator.Creator
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.domain.models.PlayerParams
import com.example.yp_playlist_maker.domain.models.Track
import com.example.yp_playlist_maker.domain.use_case.PlayTrackUseCase
import com.example.yp_playlist_maker.presentation.application.gone

class AudioPlayerActivity : AppCompatActivity() {

    private var url: String = EMPTY_STRING
    private lateinit var play: Button
    private lateinit var timer: TextView
    private val converter = Converter()
    private lateinit var playTrack: PlayTrackUseCase
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

        url = getTrackExtra?.previewUrl.toString()
        play = findViewById(R.id.play)
        play.alpha = ALPHA_25
        timer = findViewById(R.id.play_time)

        playerParams = PlayerParams(url, play, timer)
        playTrack = Creator.providePlayTrackUseCase(playerParams)

        playTrack.preparePlayer()
        play.setOnClickListener { playTrack.playBackControl() }

        Glide.with(this)
            .load(converter.convertUrl(getTrackExtra?.artworkUrl100.toString()))
            .centerCrop()
            .transform(RoundedCorners(converter.dpToPx(PLAYER_IMAGE_RADIUS)))
            .placeholder(R.drawable.img_placeholder_audio_player)
            .into(trackImage)

        trackNameTextView.text = getTrackExtra?.trackName
        artistNameTextView.text = getTrackExtra?.artistName
        trackDuration.text = converter.convertMillis((getTrackExtra?.trackTimeMillis.toString()))

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
        playTrack.pause()
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
    }
}