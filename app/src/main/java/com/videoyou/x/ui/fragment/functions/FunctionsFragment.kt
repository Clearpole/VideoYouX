package com.videoyou.x.ui.fragment.functions

import com.videoyou.x._utils.base.BaseFragment
import com.videoyou.x.databinding.FragmentMainFunctionsBinding

class FunctionsFragment:BaseFragment<FragmentMainFunctionsBinding>() {
    override fun onViewCreate() {

    }

    override fun getViewBinding(): FragmentMainFunctionsBinding {
        return FragmentMainFunctionsBinding.inflate(layoutInflater)
    }
}