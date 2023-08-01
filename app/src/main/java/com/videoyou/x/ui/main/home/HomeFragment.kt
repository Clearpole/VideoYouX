package com.videoyou.x.ui.main.home

import com.videoyou.x._utils.BaseFragment
import com.videoyou.x.databinding.FragmentMainHomeBinding

class HomeFragment : BaseFragment<HomeViewModel, FragmentMainHomeBinding>() {

    override fun onViewCreate() {
    }

    override fun getViewBinding(): FragmentMainHomeBinding {
        return FragmentMainHomeBinding.inflate(layoutInflater)
    }
}