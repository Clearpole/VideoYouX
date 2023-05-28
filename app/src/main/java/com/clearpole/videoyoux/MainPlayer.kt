package com.clearpole.videoyoux

import android.os.Bundle
import com.clearpole.videoyoux.databinding.ActivityMainPlayerBinding
import com.clearpole.videoyoux.databinding.ActivityMainPlayerLandBinding
import com.drake.serialize.intent.bundle
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder
import com.shuyu.gsyvideoplayer.cache.CacheFactory
import com.shuyu.gsyvideoplayer.cache.ProxyCacheManager
import com.shuyu.gsyvideoplayer.model.VideoOptionModel
import com.shuyu.gsyvideoplayer.player.IjkPlayerManager
import com.shuyu.gsyvideoplayer.player.PlayerFactory
import tv.danmaku.ijk.media.player.IjkMediaPlayer

class MainPlayer : BaseActivity<ActivityMainPlayerBinding, ActivityMainPlayerLandBinding>(
    isHideStatus = true,
    isLandScape = false,
    ActivityMainPlayerBinding::inflate,
    ActivityMainPlayerLandBinding::inflate
) {
    private val uri:String by bundle()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PlayerFactory.setPlayManager(IjkPlayerManager::class.java)
        IjkPlayerManager.setLogLevel(IjkMediaPlayer.IJK_LOG_SILENT)
        CacheFactory.setCacheManager(ProxyCacheManager::class.java)
        val list = arrayListOf(
            VideoOptionModel(
                IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 10
            ),
            VideoOptionModel(
                IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1
            ),
            VideoOptionModel(
                IjkMediaPlayer.OPT_CATEGORY_PLAYER,
                "enable-accurate-seek",
                1
            )
        )
        GSYVideoManager.instance().optionModelList = list
        if (landScape){

        }else{
            videoPlayer()
        }
    }
    private fun videoPlayer(){
        GSYVideoOptionBuilder().setCacheWithPlay(false).setUrl(uri)
            .setStartAfterPrepared(true).setAutoFullWithSize(true).build(binding.player)
    }
}