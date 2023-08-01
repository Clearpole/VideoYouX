package com.videoyou.x

import android.os.Bundle
import com.drake.serialize.intent.openActivity
import com.videoyou.x._utils.BaseActivity
import com.videoyou.x.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>(
    false,
    false,
    false,
    ActivityMainBinding::inflate
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
}