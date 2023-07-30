package com.clearpole.videoyoux._player

import androidx.media3.exoplayer.ExoPlayer

class Media3PlayerUtils {
    companion object{
        lateinit var exoPlayer : ExoPlayer
        fun getIfExoExist():Boolean{
            return this::exoPlayer.isInitialized
        }
    }
}