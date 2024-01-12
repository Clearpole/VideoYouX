package com.videoyou.x.ui.fragment.guide.model

import com.drake.brv.BindingAdapter
import com.drake.brv.item.ItemBind
import com.videoyou.x.R
import com.videoyou.x.databinding.ItemLanguageListBinding
import com.videoyou.x.ui.fragment.guide.GuideWelcomeFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class LanguageListModel(
    private val locale: Locale
) : ItemBind {
    override fun onBind(holder: BindingAdapter.BindingViewHolder) {
        holder.getBinding<ItemLanguageListBinding>().apply {
            if (holder.layoutPosition == 0) {
                title.text = holder.context.getString(R.string.follow_system)
                info.text = holder.context.getString(
                    R.string.follow_system_info,
                    locale.displayName,
                    locale.toLanguageTag()
                )
            } else {
                title.text = locale.displayName
                info.text = locale.toLanguageTag()
            }
            root.setOnClickListener {

            }
        }
    }
}