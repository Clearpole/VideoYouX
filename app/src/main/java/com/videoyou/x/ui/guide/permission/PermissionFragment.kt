package com.videoyou.x.ui.guide.permission

import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.navigation.navOptions
import com.videoyou.x.R
import com.videoyou.x._utils.BaseFragment
import com.videoyou.x.databinding.FragmentGuidePermissionBinding

class PermissionFragment :
    BaseFragment<GuidePermissionViewModel, FragmentGuidePermissionBinding>() {
    override fun onViewCreate() {
        val controller = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        binding.guidePermissionBack.setOnClickListener {
            controller.navigate(R.id.welcomeFragment, bundleOf(), navOptions {
                anim {
                    enter = R.anim.guide_back_in
                    exit = R.anim.guide_back_out
                }
            })
        }
    }

    override fun getViewBinding(): FragmentGuidePermissionBinding {
        return FragmentGuidePermissionBinding.inflate(layoutInflater)
    }

}