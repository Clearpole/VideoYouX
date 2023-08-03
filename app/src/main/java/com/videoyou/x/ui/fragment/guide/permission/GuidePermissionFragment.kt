package com.videoyou.x.ui.fragment.home.guide.permission

import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.navigation.navOptions
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.hjq.permissions.Permission
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
            arrayListOf(
                getString(R.string.read_video_permission) to getString(R.string.permission_use_storage) to R.drawable.outline_topic_24 to Permission.READ_MEDIA_VIDEO,
                getString(R.string.pic_in_pic_permission) to getString(R.string.usage_pic_in_pic_per) to R.drawable.baseline_picture_in_picture_alt_24 to Permission.PICTURE_IN_PICTURE,
                getString(R.string.other_per) to getString(R.string.usage_other_per) to R.drawable.outline_info_24 to "",
            ).onEach {
                add(PermissionListModel(mViewModel,it.first.first.first,it.first.first.second,it.first.second,it.second))
            }
        }
    }

    override fun getViewBinding(): FragmentGuidePermissionBinding {
        return FragmentGuidePermissionBinding.inflate(layoutInflater)
    }
}
