package com.videoyou.x.ui.fragment.main.model

import android.graphics.drawable.Drawable
import com.bumptech.glide.RequestBuilder
import com.drake.brv.BindingAdapter
import com.drake.brv.item.ItemBind
import com.videoyou.x.databinding.ItemMainCarouselBinding
import com.videoyou.x.databinding.ItemMainFoldersBinding

data class FoldersModel(private val title:String,private val updateTime:String):ItemBind {
    override fun onBind(vh: BindingAdapter.BindingViewHolder) {
        vh.getBinding<ItemMainFoldersBinding>().apply {
            foldersTitle.text = title
            foldersInfo.text = updateTime
        }
    }
}