package com.clearpole.videoyoux

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.clearpole.videoyoux.databinding.ActivityMainPlayerBinding
import com.drake.serialize.intent.bundle
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder

class MainPlayer : AppCompatActivity() {
    private val uri: String by bundle()
    private lateinit var binding: ActivityMainPlayerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_BAR).init()
        videoPlayer()
    }

    private fun videoPlayer() {
        GSYVideoOptionBuilder().setCacheWithPlay(false).setUrl(uri).setAutoFullWithSize(true).setLooping(true)
            .apply {
                build(binding.player)
            }
        binding.player.startPlayLogic()
    }

    override fun onBackPressed() {
        GSYVideoManager.releaseAllVideos()
        finish()
    }
}