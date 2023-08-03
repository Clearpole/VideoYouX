package com.videoyou.x.ui.fragment.home.guide.permission

import androidx.lifecycle.ViewModel
import com.google.android.material.shape.ShapeAppearanceModel

class GuidePermissionViewModel : ViewModel() {
    val topCorner = ShapeAppearanceModel().toBuilder().setTopLeftCornerSize { 65f }
        .setTopRightCornerSize { 65f }.setBottomLeftCornerSize { 10f }
        .setBottomRightCornerSize { 10f }.build()
    val bottomCorner = ShapeAppearanceModel().toBuilder().setTopLeftCornerSize { 10f }
        .setTopRightCornerSize { 10f }.setBottomLeftCornerSize { 65f }
        .setBottomRightCornerSize { 65f }.build()
    val centerCorner = ShapeAppearanceModel().toBuilder().setAllCornerSizes(10f).build()
    var readPermission = false
    var picPermission = false
}