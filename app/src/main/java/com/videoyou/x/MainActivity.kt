package com.videoyou.x

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import com.drake.serialize.intent.openActivity
import com.videoyou.x.R
import com.videoyou.x._utils.base.BaseActivity
import com.videoyou.x.databinding.ActivityMainBinding
import com.videoyou.x.GuideActivity

class MainActivity : BaseActivity<ActivityMainBinding>(
    isHideStatus = false
) {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("values", MODE_PRIVATE)
        val isFirstEnter = sharedPreferences.getBoolean("first-enter", true)
        if (isFirstEnter) {
            openActivity<GuideActivity>()
        }

    }

    override fun getLayout(): Int {
        return  R.layout.activity_main
    }
}