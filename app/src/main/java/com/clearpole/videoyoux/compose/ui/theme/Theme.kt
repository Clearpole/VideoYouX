package com.clearpole.videoyoux.compose.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import com.clearpole.videoyoux.utils.System
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar


@Composable
fun VideoYouXTheme(
    hideBar: Boolean,
    content: @Composable () -> Unit
) {

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            if (hideBar) {
                ImmersionBar.with(view.context as Activity).hideBar(BarHide.FLAG_HIDE_BAR).init()
            } else {
                ImmersionBar.with(view.context as Activity)
                    .statusBarDarkFont(!System.isNightMode(view.resources)).transparentBar().init()
            }
        }
    }

    MaterialTheme(
        typography = Typography,
        content = content
    )
}