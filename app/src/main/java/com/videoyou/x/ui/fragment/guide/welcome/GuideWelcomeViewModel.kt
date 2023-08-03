package com.videoyou.x.ui.fragment.home.guide.welcome

import android.view.View
import androidx.lifecycle.ViewModel
import java.util.Locale
import kotlin.properties.Delegates

class GuideWelcomeViewModel : ViewModel() {
    var animIsRunning = false
    var startView by Delegates.notNull<View>()
    var endView by Delegates.notNull<View>()
    var rootView by Delegates.notNull<View>()
    var maskView by Delegates.notNull<View>()
    var duration by Delegates.notNull<Long>()
    var maskColor by Delegates.notNull<Int>()
    var isChoseLanguage = false
    var choseLocale by Delegates.notNull<Locale>()
}