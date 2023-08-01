package com.videoyou.x.ui.home

import com.videoyou.x.BaseFragment
import com.videoyou.x.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    override fun onViewCreate() {
        binding.toolbar.title = "Nh"
    }

    override fun getViewBinding(): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater)
    }
}