package com.clearpole.videoyoux.models

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.clearpole.videoyoux.databinding.MainPageCarouselItemBinding
import com.drake.brv.BindingAdapter
import com.drake.brv.item.ItemBind
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class CarouselModel(private val uri: String) : ItemBind {
    override fun onBind(holder: BindingAdapter.BindingViewHolder) {
        holder.getBinding<MainPageCarouselItemBinding>().apply {
            CoroutineScope(Dispatchers.IO).launch {
                val load = Glide.with(holder.context).load(uri)
                    .transition(DrawableTransitionOptions.withCrossFade()).diskCacheStrategy(
                        DiskCacheStrategy.RESOURCE
                    )
                withContext(Dispatchers.Main) {
                    load.into(carouselImageView)
                }
                cancel()
            }
        }
    }
}