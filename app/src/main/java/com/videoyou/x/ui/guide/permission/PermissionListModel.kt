package com.videoyou.x.ui.guide.permission

import com.drake.brv.BindingAdapter
import com.drake.brv.item.ItemBind
import com.videoyou.x.databinding.ItemPermissionListBinding

class PermissionListModel(private val dataModel: GuidePermissionViewModel) : ItemBind {
    override fun onBind(holder: BindingAdapter.BindingViewHolder) {
        val corner = when (holder.layoutPosition) {
            0 -> {
                dataModel.topCorner
            }

            holder.bindingAdapter!!.itemCount - 1 -> {
                dataModel.bottomCorner
            }

            else -> {
                dataModel.centerCorner
            }
        }
        holder.getBinding<ItemPermissionListBinding>().apply {
            root.shapeAppearanceModel = corner
        }
    }
}