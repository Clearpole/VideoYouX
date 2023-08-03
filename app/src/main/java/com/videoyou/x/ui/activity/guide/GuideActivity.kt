package com.videoyou.x.ui.activity.guide

import android.os.Bundle
import com.videoyou.x.R
import com.videoyou.x._utils.BaseActivity
import com.videoyou.x.databinding.ActivityGuideBinding

class GuideActivity : BaseActivity<ActivityGuideBinding>(
) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun getLayout(): Int {
        return R.layout.activity_guide
    }

}