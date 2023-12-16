package com.videoyou.x._utils.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.color.DynamicColors


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
        enableEdgeToEdge()
        window.isNavigationBarContrastEnforced =false
        setContentView(binding.root)
    }

    abstract fun getLayout(): Int
}

