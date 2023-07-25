package com.clearpole.videoyoux._models

import android.graphics.drawable.Drawable
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import com.clearpole.videoyoux.R
import com.clearpole.videoyoux.databinding.ItemGuidePermissionBinding
import com.drake.brv.BindingAdapter
import com.drake.brv.item.ItemBind
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions

class GuidePermissionModel(
    private val permission: String,
    private val title: String,
    private val info: String,
    private val icon: Drawable?
) : ItemBind {
    override fun onBind(holder: BindingAdapter.BindingViewHolder) {
        val binding = holder.getBinding<ItemGuidePermissionBinding>()
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
                        Toast.makeText(
                            holder.context,
                            "权限获取中断：${permission.replace("android.permission.", "")}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
        }
    }
}