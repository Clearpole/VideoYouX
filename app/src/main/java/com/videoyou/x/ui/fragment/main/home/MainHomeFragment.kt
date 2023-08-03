package com.videoyou.x.ui.fragment.main.home

import com.videoyou.x._utils.base.BaseFragment
import com.videoyou.x.databinding.FragmentMainHomeBinding

class MainHomeFragment : BaseFragment<MainHomeViewModel, FragmentMainHomeBinding>() {

    override fun onViewCreate() {
    }

    override fun getViewBinding(): FragmentMainHomeBinding {
        return FragmentMainHomeBinding.inflate(layoutInflater)
    }
}