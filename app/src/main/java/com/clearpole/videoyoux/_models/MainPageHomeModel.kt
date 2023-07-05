package com.clearpole.videoyoux._models

import android.annotation.SuppressLint
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.clearpole.videoyoux.databinding.MainPageCarouselItemBinding
import com.clearpole.videoyoux.databinding.MainPageRvItemLandBinding
import com.clearpole.videoyoux._compose.MainPlayerActivity
import com.drake.brv.BindingAdapter
import com.drake.brv.item.ItemBind
import com.drake.serialize.intent.openActivity
import com.drake.tooltip.toast
import com.google.android.material.animation.AnimationUtils
import com.google.android.material.carousel.MaskableFrameLayout
import com.google.android.material.math.MathUtils.lerp
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class MainPageHomeModel(
    private val paths: String,
    private val uri: String,
    private val titleString: String,
    private val videoPlayer: StandardGSYVideoPlayer?,
    private val land: Boolean
) : ItemBind {
    @SuppressLint("RestrictedApi")
    override fun onBind(holder: BindingAdapter.BindingViewHolder) {
        CoroutineScope(Dispatchers.IO).launch {
            val load = Glide.with(holder.context).load(uri)
                .transition(DrawableTransitionOptions.withCrossFade()).diskCacheStrategy(
                    DiskCacheStrategy.RESOURCE
                )
            var titleStringHandled = titleString
            var subTitleStringHandled = titleString
            val regex = arrayListOf("-", "_", " ")
            for (one in regex) {
                if (titleString.contains(one)) {
                    titleString.split(one).apply {
                        titleStringHandled = this[0]
                        subTitleStringHandled = titleString.split(this[0] + one)[1]
                    }
                }
            }
            withContext(Dispatchers.Main) {
                if (land) {
                    holder.getBinding<MainPageRvItemLandBinding>().apply {
                        load.into(carouselImageView)
                        title.text = titleStringHandled
                        subTitle.text = subTitleStringHandled
                        root.setOnClickListener {
                            GSYVideoOptionBuilder().setCacheWithPlay(false).setUrl(uri).build(videoPlayer)
                            videoPlayer!!.startPlayLogic()
                        }
                    }
                } else {
                    holder.getBinding<MainPageCarouselItemBinding>().apply {
                        if (title.text.isEmpty()) {
                            (holder.itemView as MaskableFrameLayout).setOnMaskChangedListener {
                                carouselMask.translationX = it.left
                                carouselMask.alpha = AnimationUtils.lerp(1F, 0F, 0F, 130F, it.left)
                            }
                            load.into(carouselImageView)
                            title.text = titleStringHandled
                            subTitle.text = subTitleStringHandled
                            carouselItemContainer.setOnClickListener {
                                holder.context.openActivity<MainPlayerActivity>(
                                    "uri" to uri,
                                    "paths" to paths
                                )
                            }
                            carouselItemContainer.setOnLongClickListener {
                                toast(paths)
                                true
                            }
                        }
                    }
                }
            }
            cancel()
        }
    }
}