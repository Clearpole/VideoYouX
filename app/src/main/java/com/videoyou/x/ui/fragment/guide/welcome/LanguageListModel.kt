package com.videoyou.x.ui.fragment.guide.welcome

import com.drake.brv.BindingAdapter
import com.drake.brv.item.ItemBind
import com.videoyou.x.R
import com.videoyou.x.databinding.ItemLanguageListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class LanguageListModel(
    private val locale: Locale,
    private val dataModel: GuideWelcomeViewModel
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
                CoroutineScope(Dispatchers.IO).launch {
                    if (dataModel.animIsRunning) {
                        delay(550)
                    }
                    withContext(Dispatchers.Main) {
                        dataModel.isChoseLanguage = true
                        dataModel.choseLocale = locale
                        GuideWelcomeFragment.materialTransition(
                            dataModel.endView,
                            dataModel.startView,
                            dataModel.rootView,
                            dataModel.maskView,
                            dataModel.duration,
                            false,
                            dataModel.maskColor,
                            dataModel
                        )
                        cancel()
                    }
                }
            }
        }
    }
}