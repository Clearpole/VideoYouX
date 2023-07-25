package com.clearpole.videoyoux

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.clearpole.videoyoux._utils.System.Companion.isNightMode
import com.google.android.material.color.DynamicColors
import com.google.android.material.color.DynamicColorsOptions
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar


abstract class BaseActivity<VB : ViewBinding>(
    val isHideStatus: Boolean,
    val isLandScape: Boolean,
    val isRequireLightBarText: Boolean? = null,
    val isRequireDarkBarText: Boolean? = null,
    private val inflate: (LayoutInflater) -> VB,
) : AppCompatActivity() {
    lateinit var binding: VB
    var landScape: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // 安卓版本大于12
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