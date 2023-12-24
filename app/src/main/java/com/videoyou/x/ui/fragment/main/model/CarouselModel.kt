package com.videoyou.x.ui.fragment.main.model

import android.graphics.drawable.Drawable
import com.bumptech.glide.RequestBuilder
import com.drake.brv.BindingAdapter
import com.drake.brv.item.ItemBind
import com.videoyou.x.databinding.ItemMainCarouselBinding

data class CarouselModel(private val img:RequestBuilder<Drawable>):ItemBind {
    override fun onBind(vh: BindingAdapter.BindingViewHolder) {
        vh.getBinding<ItemMainCarouselBinding>().apply {
            img.into(carouselImageView)
        }
    }
}