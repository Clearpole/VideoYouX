package com.videoyou.x.utils.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.color.DynamicColors


abstract class BaseActivity<VB : ViewDataBinding> : AppCompatActivity() {

    lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DynamicColors.applyToActivityIfAvailable(this)
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(this),
            getLayout(), null, false
        )
        enableEdgeToEdge()
        window.isNavigationBarContrastEnforced = false
        setContentView(binding.root)
    }

    abstract fun getLayout(): Int
}

