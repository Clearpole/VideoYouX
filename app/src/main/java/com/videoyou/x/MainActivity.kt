package com.videoyou.x

import android.os.Bundle
import com.videoyou.x.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>(
    false,
    false,
    false,
    ActivityMainBinding::inflate
) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}