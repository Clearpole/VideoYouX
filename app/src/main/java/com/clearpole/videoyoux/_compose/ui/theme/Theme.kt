package com.clearpole.videoyoux._compose.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import com.clearpole.videoyoux._utils.System
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar

private val darkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val lightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

@Composable
fun VideoYouXTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    hideBar: Boolean,
    darkBar: Boolean = false,
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> darkColorScheme
        else -> lightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            if (hideBar) {
                ImmersionBar.with(view.context as Activity).hideBar(BarHide.FLAG_HIDE_BAR).init()
            } else {
                if (!darkBar) {
                    ImmersionBar.with(view.context as Activity)
                        .statusBarDarkFont(!System.isNightMode(view.resources)).transparentBar()
                        .init()
                } else {
                    ImmersionBar.with(view.context as Activity)
                        .statusBarDarkFont(false).transparentBar()
                        .init()
                }
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}