package com.clearpole.videoyoux

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.clearpole.videoyoux.utils.System.Companion.isNightMode
import com.dylanc.viewbinding.base.ViewBindingUtil
import com.google.android.material.color.DynamicColors
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar


abstract class BaseActivity<VB : ViewBinding>(val isHideStatus: Boolean,private val inflate: (LayoutInflater) -> VB) : AppCompatActivity() {
    lateinit var binding: VB
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // 安卓版本大于12
            DynamicColors.applyToActivityIfAvailable(this)
        } else {
            TODO("安卓版本小于12的主题")
        }
        binding = inflate(layoutInflater)
        if (isHideStatus) {
            ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_BAR)
                .init()
        } else {
            ImmersionBar.with(this).transparentBar().statusBarDarkFont(!isNightMode(resources))
                .init()
        }
        setContentView(binding.root)
    }
}