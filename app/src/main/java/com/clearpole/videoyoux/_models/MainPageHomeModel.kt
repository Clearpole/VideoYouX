package com.clearpole.videoyoux._models

import android.annotation.SuppressLint
import android.net.Uri
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.clearpole.videoyoux.databinding.MainPageRvItemLandBinding
import com.clearpole.videoyoux._compose.MainPlayerActivity
import com.clearpole.videoyoux.databinding.ItemMainCarouselBinding
import com.drake.brv.BindingAdapter
import com.drake.brv.item.ItemBind
import com.drake.serialize.intent.openActivity
import com.google.android.material.animation.AnimationUtils
import com.google.android.material.carousel.MaskableFrameLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class MainPageHomeModel(
    private val paths: String,
    private val uri: Uri,
    private val titleString: String,
    private val land: Boolean
) : ItemBind {
    @SuppressLint("RestrictedApi")
    override fun onBind(holder: BindingAdapter.BindingViewHolder) {
        CoroutineScope(Dispatchers.IO).launch {
            val load = Glide.with(holder.context).load(uri)
                .transition(DrawableTransitionOptions.withCrossFade()).diskCacheStrategy(
                    DiskCacheStrategy.ALL
                ).override(500,500).centerCrop()
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
                            
                        }
                    }
                } else {
                    holder.getBinding<ItemMainCarouselBinding>().apply {
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
                        }
                    }
                }
            }
            cancel()
        }
    }
}