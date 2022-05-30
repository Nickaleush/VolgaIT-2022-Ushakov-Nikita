package com.example.volgaitushakov.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.WindowManager
import androidx.core.content.ContextCompat
import java.io.File

fun Context.getColorCompat(resourceId: Int): Int {
    return ContextCompat.getColor(this, resourceId)
}

fun Context.dpToPxF(dp: Int): Float {
    return Device.dpToPx(this, dp)
}
