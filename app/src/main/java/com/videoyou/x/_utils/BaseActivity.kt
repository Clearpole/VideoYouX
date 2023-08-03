package com.videoyou.x._utils

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.color.DynamicColors
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar
import com.videoyou.x._utils.SystemUtils.Companion.isNightMode


abstract class BaseActivity<VB : ViewDataBinding>(
    private val isHideStatus: Boolean? = null,
    private val isRequireLightBarText: Boolean? = null,
    private val isRequireDarkBarText: Boolean? = null
) : AppCompatActivity() {

    lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DynamicColors.applyToActivityIfAvailable(this)
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(this),
            getLayout(), null, false
        )
        if (isHideStatus!!) {
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

    abstract fun getLayout(): Int
}

