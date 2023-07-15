package com.clearpole.videoyoux._assembly

import android.view.View
import androidx.viewpager.widget.ViewPager

class ViewPageAnimation : ViewPager.PageTransformer {
    //最小透明度
    private val MIN_SCALE = 0.8f

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
