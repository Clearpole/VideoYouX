package com.videoyou.x.ui.guide.welcome

import com.blankj.utilcode.util.LanguageUtils
import com.drake.brv.BindingAdapter
import com.drake.brv.item.ItemBind
import com.videoyou.x.databinding.ItemLanguageListBinding
import java.util.Locale

class LanguageListModel(
    private val locale: Locale,
    private val dataModel: GuideWelcomeViewModel
) : ItemBind {
    override fun onBind(holder: BindingAdapter.BindingViewHolder) {
        holder.getBinding<ItemLanguageListBinding>().apply {
            title.text = locale.displayName
            info.text = locale.toLanguageTag()
            root.setOnClickListener {
                LanguageUtils.applyLanguage(locale,true)
            }
        }
    }
}