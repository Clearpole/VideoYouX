package com.videoyou.x.ui.fragment.guide.model

import android.view.View
import androidx.lifecycle.ViewModel
import com.google.android.material.shape.ShapeAppearanceModel
import java.util.Locale
import kotlin.properties.Delegates

class GuideViewModel : ViewModel() {
    val topCorner = ShapeAppearanceModel().toBuilder().setTopLeftCornerSize { 65f }
        .setTopRightCornerSize { 65f }.setBottomLeftCornerSize { 10f }
        .setBottomRightCornerSize { 10f }.build()
    val bottomCorner = ShapeAppearanceModel().toBuilder().setTopLeftCornerSize { 10f }
        .setTopRightCornerSize { 10f }.setBottomLeftCornerSize { 65f }
        .setBottomRightCornerSize { 65f }.build()
    val centerCorner = ShapeAppearanceModel().toBuilder().setAllCornerSizes(10f).build()
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