package com.videoyou.x.ui.fragment.guide

import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.navigation.navOptions
import com.videoyou.x.R
import com.videoyou.x._utils.base.BaseFragment
import com.videoyou.x.databinding.FragmentGuideReadDataBinding
import com.videoyou.x.ui.fragment.guide.model.GuideViewModel

class GuideReadDataFragment : BaseFragment<GuideViewModel, FragmentGuideReadDataBinding>() {
    override fun onViewCreate() {
        val controller = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        binding.guideReadDataBack.setOnClickListener {
            controller.navigate(R.id.guidePermissionFragment, bundleOf(), navOptions {
                anim {
                    enter = R.anim.guide_back_in
                    exit = R.anim.guide_back_out
                }
            })
        }
    }

    override fun getViewBinding(): FragmentGuideReadDataBinding {
        return FragmentGuideReadDataBinding.inflate(layoutInflater)
    }

}