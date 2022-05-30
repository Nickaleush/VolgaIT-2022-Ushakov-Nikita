package com.example.volgaitushakov.utils

import android.content.Context
import android.util.DisplayMetrics

object Device {

    @JvmStatic
    fun dpToPx(context: Context, dp: Int): Float {
        val displayMetrics = context.resources.displayMetrics
        return dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)
    }

}