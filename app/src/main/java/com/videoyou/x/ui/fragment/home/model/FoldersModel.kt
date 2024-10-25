package com.videoyou.x.ui.fragment.home.model

import androidx.fragment.app.FragmentManager
import com.drake.brv.BindingAdapter
import com.drake.brv.item.ItemBind
import com.videoyou.x.databinding.ItemMainFoldersBinding
import com.videoyou.x.ui.fragment.home.ModalBottomSheet

data class FoldersModel(
    private val title: String,
    private val updateTime: String,
    private val fragmentManager: FragmentManager,
    private val path: String,
    private val count: Int
) : ItemBind {
    override fun onBind(vh: BindingAdapter.BindingViewHolder) {
        vh.getBinding<ItemMainFoldersBinding>().apply {
            foldersTitle.text = title
            foldersInfo.text = updateTime
            count.text = count.toString()
                root.setOnClickListener {
                    val modalBottomSheet = ModalBottomSheet(path)
                    modalBottomSheet.show(fragmentManager, ModalBottomSheet.TAG)
                }
        }
    }
}