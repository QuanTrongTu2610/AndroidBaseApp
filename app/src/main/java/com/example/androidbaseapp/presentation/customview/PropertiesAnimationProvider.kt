package com.example.androidbaseapp.presentation.customview

import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Color
import android.view.View
import android.view.ViewGroup

object PropertiesAnimationProvider {
    fun getViewChangeHeightAnimation(
        view: View,
        currentHeight: Int,
        destinationHeight: Int,
        duration: Long
    ): Animator {
        val anim = ValueAnimator.ofInt(currentHeight, destinationHeight)
        anim.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Int
            val layoutParams: ViewGroup.LayoutParams = view.layoutParams
            layoutParams.height = value
            view.layoutParams = layoutParams
        }
        anim.duration = duration
        return anim
    }

    fun getViewChangeWidthAnimation(
        view: View,
        currentWidth: Int,
        destinationWidth: Int,
        duration: Long
    ): Animator {
        val desWidth = if (destinationWidth == 0) 1 else destinationWidth
        val anim = ValueAnimator.ofInt(currentWidth, desWidth)
        anim.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Int
            val layoutParams: ViewGroup.LayoutParams = view.layoutParams
            layoutParams.width = value
            view.layoutParams = layoutParams
        }
        anim.duration = duration
        return anim
    }

    fun changeBackGroundColorAnimation(
        view: View,
        currentColor: Int,
        destinationColor: Int,
        duration: Long
    ): Animator {
        val anim = ValueAnimator.ofObject(
            ArgbEvaluator(),
            currentColor,
            destinationColor
        )
        anim.addUpdateListener { animator ->
            view.setBackgroundColor(
                (animator.animatedValue as Int)
            )
        }
        anim.duration = duration
        return anim
    }

    fun moveHorizontalAnimation(
        viewShouldAnimate: View,
        destinationLabel: View,
        duration: Long,
        minusDistance: Float
    ): Animator {
        val anim = ObjectAnimator.ofFloat(
            viewShouldAnimate,
            "translationX",
            destinationLabel.x - minusDistance
        )
        anim.apply {
            this.duration = duration
        }
        return anim
    }
}