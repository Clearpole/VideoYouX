package com.videoyou.x.ui.guide.welcome

import com.drake.brv.BindingAdapter
import com.drake.brv.item.ItemBind
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
            title.text = locale.displayName
            info.text = locale.toLanguageTag()
            root.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    if (dataModel.animIsRunning) {
                        delay(550)
                    }
                    withContext(Dispatchers.Main){
                        dataModel.isChoseLanguage = true
                        dataModel.choseLocale = locale
                        WelcomeFragment.materialTransition(
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