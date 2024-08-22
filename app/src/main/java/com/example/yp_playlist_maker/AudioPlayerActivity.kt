package com.example.yp_playlist_maker

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class AudioPlayerActivity : AppCompatActivity() {

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
        val trackImageCornerRadius = 8
        val trackAlbumIntent = intent.getStringExtra(Track.COLLECTION_NAME)

        Glide.with(this)
            .load(intent.getStringExtra(Track.ARTWORK_URL_500))
            .centerCrop()
            .transform(RoundedCorners(Converter().dpToPx(trackImageCornerRadius)))
            .placeholder(R.drawable.img_placeholder_audio_player)
            .into(trackImage)

        trackNameTextView.text = intent.getStringExtra(Track.TRACK_NAME)
        artistNameTextView.text = intent.getStringExtra(Track.ARTIST_NAME)
        trackDuration.text = intent.getStringExtra(Track.TRACK_TIME_MILLIS)

        if (trackAlbumIntent.isNullOrEmpty()) {
            trackAlbum.visibility = View.GONE
            trackAlbumValue.visibility = View.GONE
        } else {
            trackAlbumValue.text = trackAlbumIntent
        }

        trackYear.text = intent.getStringExtra(Track.RELEASE_DATE)
        trackGenre.text = intent.getStringExtra(Track.PRIMARY_GENRE_NAME)
        trackCountry.text = intent.getStringExtra(Track.COUNTRY)

        backButton.setOnClickListener {
            finish()
        }
    }

}