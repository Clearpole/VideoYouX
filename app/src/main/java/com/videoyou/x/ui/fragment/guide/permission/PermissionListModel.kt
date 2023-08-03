package com.videoyou.x.ui.fragment.home.guide.permission

import androidx.appcompat.content.res.AppCompatResources
import com.drake.brv.BindingAdapter
import com.drake.brv.item.ItemBind
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.videoyou.x.R
import com.videoyou.x.databinding.ItemPermissionListBinding

class PermissionListModel(
    private val dataModel: GuidePermissionViewModel,
    private val title: String,
    private val info: String,
    private val id:Int,
    private val permission:String
) : ItemBind {
    override fun onBind(holder: BindingAdapter.BindingViewHolder) {
        val right = AppCompatResources.getDrawable(holder.context, R.drawable.round_done_24)
        holder.getBinding<ItemPermissionListBinding>().apply {
            title.text = this@PermissionListModel.title
            info.text = this@PermissionListModel.info
            icon.setImageDrawable(AppCompatResources.getDrawable(holder.context,id))
            val corner = when (holder.layoutPosition) {
                0 -> {
                    dataModel.topCorner
                }

                holder.adapter.itemCount - 1 -> {
                    more.setImageDrawable(right)
                    dataModel.bottomCorner
                }

                else -> {
                    dataModel.centerCorner
                }
            }
            root.shapeAppearanceModel = corner
            if (holder.layoutPosition!=holder.adapter.itemCount-1){
                root.setOnClickListener {
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
                                }
                            }

                            override fun onDenied(
                                permissions: MutableList<String>,
                                doNotAskAgain: Boolean
                            ) {
                                XXPermissions.startPermissionActivity(holder.context,permission)
                            }
                        })
                }
            }
        }
    }
}