package com.videoyou.x._utils

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.google.android.material.color.DynamicColors
import com.google.android.material.color.DynamicColorsOptions
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar
import com.videoyou.x._utils.SystemUtils.Companion.isNightMode

abstract class BaseActivity<VB : ViewBinding>(
    private val isHideStatus: Boolean,
    private val isRequireLightBarText: Boolean? = null,
    private val isRequireDarkBarText: Boolean? = null,
    private val inflate: (LayoutInflater) -> VB,
) : AppCompatActivity() {
    private lateinit var binding: VB
    private var landScape: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        landScape = resources.configuration.uiMode == Configuration.ORIENTATION_LANDSCAPE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            DynamicColors.applyToActivityIfAvailable(this)
        } else {
            DynamicColors.applyToActivityIfAvailable(this, DynamicColorsOptions.Builder().build())
        }
        binding = inflate(layoutInflater)
        if (isHideStatus) {
            ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_BAR)
                .init()
        } else {
            ImmersionBar.with(this).transparentBar().statusBarDarkFont(
                if (isRequireLightBarText == true) {
                    false
                } else if (isRequireDarkBarText == true) {
                    true
                } else {
                    !isNightMode(resources)
                }
            )
                .init()
        }
        setContentView(binding.root)
    }
}
