package com.clearpole.videoyoux._models

import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import com.clearpole.videoyoux.R
import com.clearpole.videoyoux.databinding.GuidePermissionListBinding
import com.drake.brv.BindingAdapter
import com.drake.brv.item.ItemBind
import com.drake.tooltip.toast
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions

class GuidePermissionModel(
    private val permission: String,
    private val title: String,
    private val info: String,
    private val icon: Drawable?
) : ItemBind {
    override fun onBind(holder: BindingAdapter.BindingViewHolder) {
        val binding = holder.getBinding<GuidePermissionListBinding>()
        binding.img.setImageDrawable(icon)
        binding.title.text = title
        binding.info.text = info
        binding.icon.setImageDrawable(
            if (XXPermissions.isGranted(holder.context, permission)) {
                AppCompatResources.getDrawable(
                    holder.context,
                    R.drawable.round_done_24
                )
            } else {
                AppCompatResources.getDrawable(holder.context, R.drawable.round_close_24)
            }
        )
        binding.root.setOnClickListener {
            XXPermissions.with(holder.context)
                .permission(permission)
                .request(object : OnPermissionCallback {
                    override fun onGranted(
                        permissions: MutableList<String>,
                        allGranted: Boolean
                    ) {
                        if (allGranted) {
                            binding.icon.setImageDrawable(
                                AppCompatResources.getDrawable(
                                    holder.context,
                                    R.drawable.round_done_24
                                )
                            )
                        }
                    }

                    override fun onDenied(
                        permissions: MutableList<String>,
                        doNotAskAgain: Boolean
                    ) {
                        toast("请自行开启权限：${permission.replace("android.permission.", "")}")
                        XXPermissions.startPermissionActivity(holder.context)
                    }
                })
        }
    }
}