package com.videoyou.x.ui.activity.main

import android.os.Bundle
import com.drake.serialize.intent.openActivity
import com.videoyou.x.R
import com.videoyou.x._utils.BaseActivity
import com.videoyou.x.databinding.ActivityMainBinding
import com.videoyou.x.ui.activity.guide.GuideActivity

class MainActivity : BaseActivity<ActivityMainBinding>(
) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("values", MODE_PRIVATE)
        val isFirstEnter = sharedPreferences.getBoolean("first-enter", true)
        if (isFirstEnter) {
            openActivity<GuideActivity>()
            finish()
        }

    }

    override fun getLayout(): Int {
        return  R.layout.activity_main
    }
}