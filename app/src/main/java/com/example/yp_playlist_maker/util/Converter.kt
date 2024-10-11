package com.example.yp_playlist_maker.util

import android.content.res.Resources
import android.util.DisplayMetrics
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.roundToInt

object Converter {

    fun dpToPx(value : Int): Int {
        val displayMetrics = Resources.getSystem().displayMetrics
        return (value * (displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT.toFloat())).roundToInt()
    }

    fun convertMillis(time: String): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(time.toInt()).toString()
    }

    fun convertUrl(url: String): String {
        return url.replaceAfterLast('/',"512x512bb.jpg")
    }
}