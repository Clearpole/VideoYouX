package com.clearpole.videoyoux._utils

import android.animation.ValueAnimator
import com.clearpole.videoyoux.Values
import com.google.android.material.slider.Slider

class AnimSlider {
    companion object {
        fun start(view: Slider, progress: Float) {
            val lastProgress= view.value
            val dh = ValueAnimator.ofFloat(lastProgress, progress)
            dh.duration = 200
            dh.addUpdateListener {
                val nowAnimatedValue = dh.animatedValue as Float
                if (nowAnimatedValue == progress) {
                    dh.cancel()
                    Values.isSystem = false
                }
                view.value = nowAnimatedValue
            }
            dh.start()
        }
    }
}