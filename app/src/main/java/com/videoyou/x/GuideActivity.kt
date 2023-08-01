package com.videoyou.x

import android.os.Bundle
import com.videoyou.x._utils.BaseActivity
import com.videoyou.x.databinding.ActivityGuideBinding

class GuideActivity : BaseActivity<ActivityGuideBinding>(
    false,
    false,
    false,
    ActivityGuideBinding::inflate
) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}