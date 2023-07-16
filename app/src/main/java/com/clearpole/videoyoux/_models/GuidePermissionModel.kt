package com.clearpole.videoyoux._models

import android.graphics.drawable.Drawable
import com.clearpole.videoyoux.databinding.GuidePermissionListBinding
import com.drake.brv.BindingAdapter
import com.drake.brv.item.ItemBind

class GuidePermissionModel(
    private val permission: String,
    private val title: String,
    private val info: String,
    private val icon:Drawable?
) : ItemBind {
    override fun onBind(holder: BindingAdapter.BindingViewHolder) {
        val binding = holder.getBinding<GuidePermissionListBinding>()
        binding.img.setImageDrawable(icon)
        binding.title.text = title
        binding.info.text = info
    }
}