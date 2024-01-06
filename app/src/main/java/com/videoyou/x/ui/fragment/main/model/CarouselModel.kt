package com.videoyou.x.ui.fragment.main.model

import android.graphics.drawable.Drawable
import androidx.core.net.toUri
import com.bumptech.glide.RequestBuilder
import com.drake.brv.BindingAdapter
import com.drake.brv.item.ItemBind
import com.drake.serialize.intent.openActivity
import com.tencent.mmkv.MMKV
import com.videoyou.x.PlayerActivity
import com.videoyou.x.PlayerActivity2
import com.videoyou.x.databinding.ItemMainCarouselBinding

data class CarouselModel(private val img:RequestBuilder<Drawable>,private val list: List<String>,private val mediaList:MMKV):ItemBind {
    override fun onBind(vh: BindingAdapter.BindingViewHolder) {
        vh.getBinding<ItemMainCarouselBinding>().apply {
            img.into(carouselImageView)
            carouselItemContainer.setOnClickListener {
               /* vh.context.openActivity<PlayerActivity>(
                    "path" to list[0],
                    "uri" to (list[1]).toUri()
                )*/
                vh.context.openActivity<PlayerActivity2>(

                )
            }
        }
    }
}