package com.videoyou.x.ui.guide.permission

import androidx.navigation.Navigation
import com.videoyou.x.R
import com.videoyou.x._utils.BaseFragment
import com.videoyou.x.databinding.FragmentGuidePermissionBinding

class PermissionFragment :
    BaseFragment<GuidePermissionViewModel, FragmentGuidePermissionBinding>() {
    override fun onViewCreate() {
        val controller = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        binding.guidePermissionBack.setOnClickListener {
            controller.navigateUp()
        }
    }

    override fun getViewBinding(): FragmentGuidePermissionBinding {
        return FragmentGuidePermissionBinding.inflate(layoutInflater)
    }

}