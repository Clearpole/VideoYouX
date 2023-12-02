package com.videoyou.x.ui.fragment.guide.model

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import com.drake.brv.BindingAdapter
import com.drake.brv.item.ItemBind
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.videoyou.x.R
import com.videoyou.x.databinding.ItemPermissionListBinding

class PermissionListModel(
    private val dataModel: GuideViewModel,
    private val title: String,
    private val info: String,
    private val resId: Int,
    private val permission: String,
    private val right: Drawable,
    private val unknown: Drawable,
    private val primary: Int,
    private val sPrimary: Int
) : ItemBind {
    override fun onBind(holder: BindingAdapter.BindingViewHolder) {
        val id = holder.layoutPosition
        val count = holder.adapter.itemCount
        holder.getBinding<ItemPermissionListBinding>().apply {
            title.text = this@PermissionListModel.title
            info.text = this@PermissionListModel.info
            icon.setImageDrawable(AppCompatResources.getDrawable(holder.context, resId))
            if (XXPermissions.isGranted(holder.context, permission) || id == count - 1) {
                ColorStateList.valueOf(primary).apply {
                    more.imageTintList = this
                    icon.imageTintList = this
                }
                more.setImageDrawable(right)
            } else {
                ColorStateList.valueOf(sPrimary).apply {
                    more.imageTintList = this
                    icon.imageTintList = this
                }
                more.setImageDrawable(unknown)
            }
            val corner = when (id) {
                0 -> {
                    dataModel.topCorner
                }

                count - 1 -> {
                    more.setImageDrawable(right)
                    dataModel.bottomCorner
                }

                else -> {
                    dataModel.centerCorner
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
                                    ColorStateList.valueOf(primary).apply {
                                        more.imageTintList = this
                                        icon.imageTintList = this
                                    }
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