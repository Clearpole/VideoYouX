package com.clearpole.videoyoux

import android.os.Bundle
import android.view.View
import com.clearpole.videoyoux.databinding.ActivityGuideBinding
import com.clearpole.videoyoux.databinding.ActivityGuideLandBinding
import com.clearpole.videoyoux.screen_home.ViewPagerAdapter

class GuideActivity : BaseActivity<ActivityGuideBinding, ActivityGuideLandBinding>(
    isHideStatus = false,
    isLandScape = false,
    isRequireLightBarText = true,
    inflate = ActivityGuideBinding::inflate,
    inflateLand = ActivityGuideLandBinding::inflate
) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewPager()
    }

    private fun viewPager() {
        val view = binding.guidePagerView
        val pagesList = ArrayList<View>()
        pagesList.apply {
            add(View.inflate(this@GuideActivity, R.layout.guide_welcome, null))
            add(View.inflate(this@GuideActivity, R.layout.guide_permission, null))
            add(View.inflate(this@GuideActivity, R.layout.guide_data_store, null))
            add(View.inflate(this@GuideActivity, R.layout.guide_settings, null))
            view.adapter = ViewPagerAdapter(this)
            view.setCanSwipe(false)
        }

        pagesList[0].apply {
        }
    }

}