package com.clearpole.videoyoux

import android.os.Bundle
import android.view.View
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.model.KeyPath
import com.clearpole.videoyoux._adapter.ViewPagerAdapter
import com.clearpole.videoyoux._assembly.ViewPageAnimation
import com.clearpole.videoyoux.databinding.ActivityGuideBinding
import com.clearpole.videoyoux.databinding.ActivityGuideLandBinding
import com.google.android.material.button.MaterialButton


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


    private fun viewPager() {
        val view = binding.guidePagerView
        val pagesList = ArrayList<View>()
        pagesList.apply {
            add(View.inflate(this@GuideActivity, R.layout.guide_welcome, null))
            add(View.inflate(this@GuideActivity, R.layout.guide_permission, null))
            add(View.inflate(this@GuideActivity, R.layout.guide_data_store, null))
            add(View.inflate(this@GuideActivity, R.layout.guide_settings, null))
            view.adapter = ViewPagerAdapter(this)
            view.setPageTransformer(true, ViewPageAnimation())
            pageList = this
        }.let {
            it[0].apply {
                findViewById<MaterialButton>(R.id.guide_getStart).setOnClickListener {
                    binding.guidePagerView.currentItem = 1
                }
            }
        }
    }

}