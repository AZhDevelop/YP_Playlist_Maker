package com.example.yp_playlist_maker

import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {

    companion object {
        const val PLAYER_IMAGE_RADIUS: Int = 8
        const val INTENT_PUTTED_TRACK: String = "PuttedTrack"
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val MILLIS_500 = 500L
        private const val DEFAULT_TIME = "00:00"
    }

    private var playerState = STATE_DEFAULT
    private var mediaPlayer = MediaPlayer()
    private var url: String = ""
    private lateinit var play: ImageView
    private lateinit var timer: TextView
    private lateinit var mainThreadHandler: Handler
    private lateinit var runnable: Runnable

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

        val getTrackExtra =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(INTENT_PUTTED_TRACK, Track::class.java)
            }
            else{
                intent.getParcelableExtra(INTENT_PUTTED_TRACK) as Track?
            }

        val trackAlbumIntent = getTrackExtra?.collectionName
        url = getTrackExtra?.previewUrl.toString()
        play = findViewById(R.id.play)

        mainThreadHandler = Handler(Looper.getMainLooper())
        runnable = Runnable {
            val trackTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition).toString()
            timer.text = trackTime
            mainThreadHandler.postDelayed(runnable, MILLIS_500)
        }
        timer = findViewById(R.id.play_time)

        preparePlayer()
        play.setOnClickListener { playbackControl() }

        Glide.with(this)
            .load(getTrackExtra?.artworkUrl100
                .toString()
                .replaceAfterLast('/',"512x512bb.jpg"))
            .centerCrop()
            .transform(RoundedCorners(Converter().dpToPx(PLAYER_IMAGE_RADIUS)))
            .placeholder(R.drawable.img_placeholder_audio_player)
            .into(trackImage)

        trackNameTextView.text = getTrackExtra?.trackName
        artistNameTextView.text = getTrackExtra?.artistName
        trackDuration.text = Converter().convertMillis((getTrackExtra?.trackTimeMillis.toString()))

        if (trackAlbumIntent.isNullOrEmpty()) {
            trackAlbum.visibility = View.GONE
            trackAlbumValue.visibility = View.GONE
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

    private fun preparePlayer() {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            play.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            play.setImageResource(R.drawable.btn_play)
            playerState = STATE_PREPARED
            mainThreadHandler.removeCallbacks(runnable)
            timer.text = DEFAULT_TIME
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
        play.setImageResource(R.drawable.btn_pause)
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
        play.setImageResource(R.drawable.btn_play)
    }

    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()
                mainThreadHandler.removeCallbacks(runnable)
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
                mainThreadHandler.post(runnable)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        mainThreadHandler.removeCallbacks(runnable)
    }

}