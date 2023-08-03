package com.videoyou.x.ui.guide.permission

import androidx.lifecycle.ViewModel
import com.google.android.material.shape.ShapeAppearanceModel

class GuidePermissionViewModel : ViewModel() {
    val topCorner = ShapeAppearanceModel().toBuilder().setTopLeftCornerSize { 50f }
        .setTopRightCornerSize { 50f }.setBottomLeftCornerSize { 10f }
        .setBottomRightCornerSize { 10f }.build()
    val bottomCorner =  ShapeAppearanceModel().toBuilder().setTopLeftCornerSize { 10f }
        .setTopRightCornerSize { 10f }.setBottomLeftCornerSize { 50f }
        .setBottomRightCornerSize { 50f }.build()
    val centerCorner = ShapeAppearanceModel().toBuilder().setAllCornerSizes(10f).build()
}