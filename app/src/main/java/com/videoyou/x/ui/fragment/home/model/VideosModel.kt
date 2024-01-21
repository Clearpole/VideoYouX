package com.videoyou.x.ui.fragment.home.model

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.drake.brv.BindingAdapter
import com.drake.brv.item.ItemBind
import com.videoyou.x.databinding.ItemVideosItemBinding

class VideosModel(
    private val videoPath: String,
    private val mVideoTitle: String,
    private val videoSize: String
) : ItemBind {
    override fun onBind(vh: BindingAdapter.BindingViewHolder) {
        vh.getBinding<ItemVideosItemBinding>().apply {
            val load = Glide.with(vh.context)
                .setDefaultRequestOptions(RequestOptions().frame(1000000)).load(videoPath)
                .transition(DrawableTransitionOptions.withCrossFade()).diskCacheStrategy(
                    DiskCacheStrategy.ALL
                ).centerCrop().override(200)
            load.into(Thumbnail)
            videoTitle.text = mVideoTitle
            val size = (videoSize.toFloat() / 1024000F)
            if (size >= 1024) {
                videoSubTitle.text = String.format("%.2f", (size / 1024F)) + " GiB"
            } else {
                videoSubTitle.text = String.format("%.2f", size) + " MiB"
            }
        }
    }
}