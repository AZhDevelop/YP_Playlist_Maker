package com.example.yp_playlist_maker.domain.models

import android.widget.Button
import android.widget.TextView

data class PlayerParams(
    val url: String,
    val play: Button,
    val timer: TextView
)
