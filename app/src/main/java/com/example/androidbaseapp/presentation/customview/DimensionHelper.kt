package com.example.androidbaseapp.presentation.customview

import android.content.Context
import android.util.DisplayMetrics

object DimensionHelper {

    fun convertDpToPixel(
        dp: Float,
        context: Context
    ): Float {
        return dp * (context.resources
            .displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun convertPixelsToDp(
        px: Float,
        context: Context
    ): Float {
        return px / (context.resources
            .displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }
}