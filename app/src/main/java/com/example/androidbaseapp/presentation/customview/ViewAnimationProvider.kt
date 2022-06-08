package com.example.androidbaseapp.presentation.customview

import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.DecelerateInterpolator

object ViewAnimationProvider {
    fun fadeAnimation(duration: Long): Animation {
        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.interpolator = DecelerateInterpolator() //add this
        fadeIn.duration = duration

        val animation = AnimationSet(false) //change to false
        animation.addAnimation(fadeIn)
        return animation
    }
}