package com.example.androidbaseapp.presentation.customview

import android.content.Context
import android.util.TypedValue
import com.example.androidbaseapp.common.Logger

object DimensionHelper {

    /**
     * This used to convert dp to pixel
     * */
    fun convertDpToPixel(
        dp: Float,
        context: Context
    ): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        )
    }

    /**
     * This used to convert sp to pixel
     * */
    fun convertSpToPixel(
        sp: Float,
        context: Context
    ): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            sp,
            context.resources.displayMetrics
        )
    }

    /**
     * This used to convert pixel to sp
     * */
    fun pixelsToSp(px: Float, context: Context): Float {
        val scaledDensity = context.resources.displayMetrics.scaledDensity
        return px / scaledDensity
    }
}