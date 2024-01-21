package com.videoyou.x.ui.fragment.functions.model

import com.drake.brv.BindingAdapter
import com.drake.brv.item.ItemBind
import com.videoyou.x.databinding.ItemFunctionsOverviewBinding
import com.videoyou.x.storage.Statistics
import kotlin.math.roundToInt

class OverViewModel(private val item: String, private val content: String) : ItemBind {
    override fun onBind(vh: BindingAdapter.BindingViewHolder) {
        vh.getBinding<ItemFunctionsOverviewBinding>().apply {
            title.text = if (content == Statistics.VIDEOS_SIZE) {
                val size = (Statistics.readInfo(content).toFloat() / 1024000F)
                if (size >= 1024) {
                    String.format("%.2f",(size/1024F)) + " GiB"
                } else {
                    String.format("%.2f",size) + " MiB"
                }
            } else {
                Statistics.readInfo(content)
            }
            subTitle.text = item
        }
    }
}