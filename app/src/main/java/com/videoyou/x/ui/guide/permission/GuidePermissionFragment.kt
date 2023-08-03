package com.videoyou.x.ui.guide.permission

import androidx.collection.arrayMapOf
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.navigation.navOptions
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.videoyou.x.R
import com.videoyou.x._utils.BaseFragment
import com.videoyou.x.databinding.FragmentGuidePermissionBinding

class GuidePermissionFragment :
    BaseFragment<GuidePermissionViewModel, FragmentGuidePermissionBinding>() {
    override fun onViewCreate() {
        val controller = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        binding.guidePermissionBack.setOnClickListener {
            controller.navigate(R.id.guideWelcomeFragment, bundleOf(), navOptions {
                anim {
                    enter = R.anim.guide_back_in
                    exit = R.anim.guide_back_out
                }
            })
        }
        binding.guidePermissionList.linear().setup {
            addType<PermissionListModel> { R.layout.item_permission_list }
        }.models = mutableListOf<Any>().apply {
           arrayMapOf(requireContext().getString(R.string.read_video_permission) to requireContext().getString(R.string.permission_use_storage),
                "画中画权限" to "用于后台小窗播放",
                "其他权限" to "自动给予小型权限以改善Vyx体验").forEach {
                    add(PermissionListModel(mViewModel))
            }
        }
    }

    override fun getViewBinding(): FragmentGuidePermissionBinding {
        return FragmentGuidePermissionBinding.inflate(layoutInflater)
    }
}
