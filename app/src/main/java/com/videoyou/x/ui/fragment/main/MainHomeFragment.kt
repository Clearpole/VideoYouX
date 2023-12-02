package com.videoyou.x.ui.fragment.main

import com.videoyou.x._utils.base.BaseFragment
import com.videoyou.x.databinding.FragmentMainHomeBinding
import com.videoyou.x.ui.fragment.main.model.MainViewModel

class MainHomeFragment : BaseFragment<MainViewModel, FragmentMainHomeBinding>() {

    override fun onViewCreate() {
    }

    override fun getViewBinding(): FragmentMainHomeBinding {
        return FragmentMainHomeBinding.inflate(layoutInflater)
    }
}