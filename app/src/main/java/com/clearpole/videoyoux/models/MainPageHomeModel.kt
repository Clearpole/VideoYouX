package com.clearpole.videoyoux.models

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.clearpole.videoyoux.databinding.MainPageCarouselItemBinding
import com.clearpole.videoyoux.databinding.MainPageRvItemLandBinding
import com.drake.brv.BindingAdapter
import com.drake.brv.item.ItemBind
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class MainPageHomeModel(
    private val uri: String,
    private val titleString: String,
    private val land: Boolean
) : ItemBind {
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
                    }
                } else {
                    holder.getBinding<MainPageCarouselItemBinding>().apply {
                        load.into(carouselImageView)
                        title.text = titleStringHandled
                        subTitle.text = subTitleStringHandled
                    }
                }
            }
            cancel()
        }
    }
}