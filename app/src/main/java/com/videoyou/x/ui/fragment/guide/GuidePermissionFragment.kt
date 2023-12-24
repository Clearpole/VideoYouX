package com.videoyou.x.ui.fragment.guide

import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.CycleInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.navigation.navOptions
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.videoyou.x.R
import com.videoyou.x._storage.AndroidMediaStore
import com.videoyou.x._utils.base.BaseFragment
import com.videoyou.x.databinding.FragmentGuidePermissionBinding
import com.videoyou.x.ui.fragment.guide.model.GuideViewModel
import com.videoyou.x.ui.fragment.guide.model.PermissionListModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GuidePermissionFragment :
    BaseFragment<GuideViewModel, FragmentGuidePermissionBinding>() {
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
        binding.guidePermissionNext.setOnClickListener {
            if (XXPermissions.isGranted(
                    requireContext(),
                    arrayListOf(Permission.READ_MEDIA_VIDEO, Permission.PICTURE_IN_PICTURE)
                )
            ) {
                val sharedPreferences = requireContext().getSharedPreferences("values",
                    AppCompatActivity.MODE_PRIVATE
                )
                sharedPreferences.edit().putBoolean("first-enter",false).apply()
                this@GuidePermissionFragment.requireActivity().finish()
            } else {
                val anim =
                    ObjectAnimator.ofFloat(
                        it,
                        View.TRANSLATION_X.name,
                        0f,
                        8f
                    )
                anim.setDuration(300)
                anim.interpolator = CycleInterpolator(4f)
                anim.start()
            }
        }
        binding.guidePermissionList.linear().setup {
            addType<PermissionListModel> { R.layout.item_permission_list }
        }.models = mutableListOf<Any>().apply {
            val right = AppCompatResources.getDrawable(requireContext(), R.drawable.round_done_24)
            val unknown =
                AppCompatResources.getDrawable(requireContext(), R.drawable.round_question_mark_24)
            val primary = requireContext().getColor(R.color.welcome_primary_color)
            val sPrimary = requireContext().getColor(R.color.welcome_primary_color_s)
            arrayListOf(
                getString(R.string.read_video_permission) to getString(R.string.read_video_permission_usage) to R.drawable.outline_topic_24 to Permission.READ_MEDIA_VIDEO,
                getString(R.string.pic_in_pic_permission) to getString(R.string.pic_in_pic_permission_usage) to R.drawable.baseline_picture_in_picture_alt_24 to Permission.PICTURE_IN_PICTURE,
                getString(R.string.other_permission) to getString(R.string.other_permission_usage) to R.drawable.outline_info_24 to "",
            ).onEach {
                add(
                    PermissionListModel(
                        mViewModel,
                        it.first.first.first,
                        it.first.first.second,
                        it.first.second,
                        it.second,
                        right!!,
                        unknown!!,
                        primary,
                        sPrimary
                    )
                )
            }
        }
    }

    override fun getViewBinding(): FragmentGuidePermissionBinding {
        return FragmentGuidePermissionBinding.inflate(layoutInflater)
    }
}
