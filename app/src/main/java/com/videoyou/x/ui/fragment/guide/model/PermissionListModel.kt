package com.videoyou.x.ui.fragment.guide.model

import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import com.drake.brv.BindingAdapter
import com.drake.brv.item.ItemBind
import com.google.android.material.shape.ShapeAppearanceModel
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.videoyou.x.R
import com.videoyou.x.databinding.ItemPermissionListBinding

class PermissionListModel(
    private val title: String,
    private val info: String,
    private val resId: Int,
    private val permission: String,
    private val right: Drawable,
    private val unknown: Drawable,
    private val topCorner:ShapeAppearanceModel,
    private val bottomCorner:ShapeAppearanceModel,
    private val centerCorner:ShapeAppearanceModel
) : ItemBind {
    override fun onBind(holder: BindingAdapter.BindingViewHolder) {
        val id = holder.layoutPosition
        val count = holder.adapter.itemCount
        holder.getBinding<ItemPermissionListBinding>().apply {
            title.text = this@PermissionListModel.title
            info.text = this@PermissionListModel.info
            icon.setImageDrawable(AppCompatResources.getDrawable(holder.context, resId))
            if (XXPermissions.isGranted(holder.context, permission) || id == count - 1) {
                more.setImageDrawable(right)
            } else {
                more.setImageDrawable(unknown)
            }
            val corner = when (id) {
                0 -> {
                    topCorner
                }

                count - 1 -> {
                    more.setImageDrawable(right)
                    bottomCorner
                }

                else -> {
                    centerCorner
                }
            }
            card.shapeAppearanceModel = corner
            if (id != count - 1) {
                card.setOnClickListener {
                    val error =
                        AppCompatResources.getDrawable(holder.context, R.drawable.round_close_24)
                    XXPermissions.with(holder.context)
                        .permission(permission)
                        .request(object : OnPermissionCallback {
                            override fun onGranted(
                                permissions: MutableList<String>,
                                allGranted: Boolean
                            ) {
                                if (allGranted) {
                                    more.setImageDrawable(
                                        right
                                    )
                                } else {
                                    more.setImageDrawable(error)
                                }
                            }

                            override fun onDenied(
                                permissions: MutableList<String>,
                                doNotAskAgain: Boolean
                            ) {
                                more.setImageDrawable(error)
                                XXPermissions.startPermissionActivity(holder.context, permission)
                            }
                        })
                }
            }
        }
    }
}