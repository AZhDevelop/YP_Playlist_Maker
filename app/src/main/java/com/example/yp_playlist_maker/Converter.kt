package com.example.yp_playlist_maker

import android.content.res.Resources
import android.util.DisplayMetrics
import kotlin.math.roundToInt

class Converter {

    fun dpToPx(value : Int): Int {
        val displayMetrics = Resources.getSystem().displayMetrics
        return (value * (displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT.toFloat())).roundToInt()
    }

}