package com.videoyou.x

import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.OptIn
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.datasource.DefaultDataSourceFactory
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.PlayerView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.videoyou.x._player.Media3PlayerUtils
import com.videoyou.x._player.ViewPager2Adapter
import com.videoyou.x._storage.AndroidMediaStore
import com.videoyou.x._utils.base.BaseActivity
import com.videoyou.x.databinding.ActivityPlayer2Binding


class PlayerActivity2 : BaseActivity<ActivityPlayer2Binding>() {
    lateinit var exoPlayer: ExoPlayer
    private var exoExist = false


    @OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        exoExist = Media3PlayerUtils.getIfExoExist()
        if (!exoExist) {
            // 如果不存在，则建立
            exoPlayer = ExoPlayer.Builder(this)
                .setRenderersFactory(DefaultRenderersFactory(this).setEnableDecoderFallback(false))
                .setUseLazyPreparation(false)
                .build()
            Media3PlayerUtils.exoPlayer = exoPlayer
        } else {
            // 如果存在，则复用
            exoPlayer = Media3PlayerUtils.exoPlayer!!
        }

        exoPlayer.repeatMode = Player.REPEAT_MODE_ONE

        exoPlayer.setMediaItems(videoLists())

        val adapter = ViewPager2Adapter()
        adapter.setData(exoPlayer)
        binding.viewPager.adapter = adapter

        binding.viewPager.registerOnPageChangeCallback((object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                exoPlayer.seekTo(position,10L)
            }
        }))
    }

    @OptIn(UnstableApi::class) private fun videoLists():ArrayList<MediaItem>{
        val dataList = AndroidMediaStore.readVideosData()
        val sortedList = dataList.allKeys()!!.sorted()
        return arrayListOf<MediaItem>().apply {
            sortedList.reversed().take(10).forEachIndexed { _, s ->
                val items = dataList.decodeString(s)!!
                val list = items.split("\u001A")
                add(MediaItem.fromUri(list[0].toUri()))
            }
        }
    }


    override fun getLayout(): Int {
        return R.layout.activity_player2
    }
}