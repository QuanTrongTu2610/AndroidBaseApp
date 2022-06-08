package com.example.androidbaseapp.presentation.customview

import android.content.Context
import android.os.Build

object ColorHelper {
    fun getColorById(context: Context, colorId: Int): Int {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                context.resources.getColor(colorId, context.theme)
            } else {
                context.resources.getColor(colorId)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }
}