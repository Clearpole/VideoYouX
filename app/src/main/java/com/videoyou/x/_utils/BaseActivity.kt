package com.videoyou.x._utils

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * 描述:
 * @author: zw
 * @time: 2023/5/22 17:33
 */
abstract class BaseActivity<VB : ViewDataBinding> : AppCompatActivity() {

    public lateinit var mBinding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this),
            getLayout(), null, false
        )
        setContentView(mBinding.root)
    }

    abstract fun getLayout(): Int
}

