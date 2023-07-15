package com.clearpole.videoyoux

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.clearpole.videoyoux.databinding.ActivityGuideBinding
import com.clearpole.videoyoux.databinding.ActivityGuideLandBinding
import com.clearpole.videoyoux.screen_home.ViewPagerAdapter


class GuideActivity : BaseActivity<ActivityGuideBinding, ActivityGuideLandBinding>(
    isHideStatus = false,
    isLandScape = false,
    isRequireLightBarText = false,
    inflate = ActivityGuideBinding::inflate,
    inflateLand = ActivityGuideLandBinding::inflate
) {
    private lateinit var pageList: ArrayList<View>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewPager()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        pageList[0].apply {
            val monetColor =
                if (newConfig.uiMode == Configuration.UI_MODE_NIGHT_YES) {

                } else {

                }
        }

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

        pageList = pagesList
    }

}