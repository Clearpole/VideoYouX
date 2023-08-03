package com.videoyou.x.ui.guide.permission

import androidx.lifecycle.ViewModel
import com.google.android.material.shape.ShapeAppearanceModel

class GuidePermissionViewModel : ViewModel() {
    val topCorner = ShapeAppearanceModel().toBuilder().setTopLeftCornerSize { 25f }
        .setTopRightCornerSize { 25f }.setBottomLeftCornerSize { 3f }
        .setBottomRightCornerSize { 3f }.build()
    val bottomCorner =  ShapeAppearanceModel().toBuilder().setTopLeftCornerSize { 3f }
        .setTopRightCornerSize { 3f }.setBottomLeftCornerSize { 25f }
        .setBottomRightCornerSize { 25f }.build()
    val centerCorner = ShapeAppearanceModel().toBuilder().setAllCornerSizes(3f).build()
}