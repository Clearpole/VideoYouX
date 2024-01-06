package com.videoyou.x._player

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.recyclerview.widget.RecyclerView
import com.videoyou.x.R

class ViewPager2Adapter : RecyclerView.Adapter<ViewPager2Adapter.MHolder>() {
    lateinit var exoPlayer: ExoPlayer
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_player_list, parent, false)
        return MHolder(itemView)
    }

    override fun onBindViewHolder(holder: MHolder, position: Int) {
        val playView = (holder.view as PlayerView)
        playView.player = exoPlayer
    }

    override fun getItemCount(): Int {
        return 10
    }

    @OptIn(UnstableApi::class)
    fun setData(mExoPlayer: ExoPlayer) {
        exoPlayer = mExoPlayer
    }

    class MHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val view = itemView.findViewById<View>(R.id.playerWindow)
    }
}