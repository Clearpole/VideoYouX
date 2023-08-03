package com.videoyou.x.ui.fragment.guide.permission

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
    private val dataModel: GuidePermissionViewModel,
    private val title: String,
    private val info: String,
    private val id:Int,
    private val permission:String,
    private val right:Drawable,
    private val unknown:Drawable,
    private val primary:Int,
    private val sPrimary:Int
) : ItemBind {
    override fun onBind(holder: BindingAdapter.BindingViewHolder) {
        holder.getBinding<ItemPermissionListBinding>().apply {
            title.text = this@PermissionListModel.title
            info.text = this@PermissionListModel.info
            icon.setImageDrawable(AppCompatResources.getDrawable(holder.context,id))
            if (XXPermissions.isGranted(holder.context,permission)){
                ColorStateList.valueOf(primary).apply {
                    more.imageTintList = this
                    icon.imageTintList = this
                }
                more.setImageDrawable(right)
            }else{
                ColorStateList.valueOf(primary).apply {
                    more.imageTintList = this
                    icon.imageTintList = this
                }
                more.setImageDrawable(unknown)
            }
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
            card.shapeAppearanceModel = corner
            if (holder.layoutPosition!=holder.adapter.itemCount-1){
                card.setOnClickListener {
                    val error = AppCompatResources.getDrawable(holder.context,R.drawable.round_close_24)
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
                                }else{
                                    more.setImageDrawable(error)
                                }
                            }

                            override fun onDenied(
                                permissions: MutableList<String>,
                                doNotAskAgain: Boolean
                            ) {
                                ColorStateList.valueOf(sPrimary).apply {
                                    more.imageTintList = this
                                    icon.imageTintList = this
                                }
                                more.setImageDrawable(error)
                                XXPermissions.startPermissionActivity(holder.context,permission)
                            }
                        })
                }
            }
        }
    }
}