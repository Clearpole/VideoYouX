package com.videoyou.x.ui.guide.permission

import com.videoyou.x._utils.BaseFragment
import com.videoyou.x.databinding.FragmentPermissionBinding

class PermissionFragment : BaseFragment<PermissionViewModel, FragmentPermissionBinding>() {
    override fun onViewCreate() {

    }

    override fun getViewBinding(): FragmentPermissionBinding {
        return FragmentPermissionBinding.inflate(layoutInflater)
    }
}