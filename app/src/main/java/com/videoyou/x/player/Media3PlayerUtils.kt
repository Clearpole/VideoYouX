package com.videoyou.x.player

import androidx.media3.exoplayer.ExoPlayer

class Media3PlayerUtils {
    companion object{
        var exoPlayer : ExoPlayer? = null
        fun getIfExoExist():Boolean{
            return exoPlayer != null
        }
    }
}