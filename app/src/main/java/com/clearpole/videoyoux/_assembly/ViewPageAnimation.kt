package com.clearpole.videoyoux._assembly

import android.content.Context
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.viewpager.widget.ViewPager
import java.lang.reflect.Field


class ViewPageAnimation : ViewPager.PageTransformer {
    //最小透明度
    private val MIN_SCALE = 0.1f

    override fun transformPage(page: View, position: Float) {
        page.apply {
            when {
                position < -1 -> {}
                position <= 1 -> {
                    val scaleValue = Math.max(MIN_SCALE, 1 - Math.abs(position))
                    scaleX = scaleValue
                    scaleY = scaleValue
                }
                else -> {}
            }
        }
    }
}

