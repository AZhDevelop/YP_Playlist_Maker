package com.example.yp_playlist_maker

import android.content.res.Resources
import android.util.DisplayMetrics
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.roundToInt

class Converter {

    fun dpToPx(value : Int): Int {
        val displayMetrics = Resources.getSystem().displayMetrics
        return (value * (displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT.toFloat())).roundToInt()
    }

    fun convertMillis(time: String): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(time.toInt()).toString()
    }

}