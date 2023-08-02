package com.videoyou.x.ui.guide.welcome

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.navigation.navOptions
import androidx.transition.Transition.TransitionListener
import androidx.transition.TransitionManager
import com.blankj.utilcode.util.LanguageUtils
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.google.android.material.transition.MaterialContainerTransform
import com.videoyou.x.R
import com.videoyou.x._utils.BaseFragment
import com.videoyou.x.databinding.FragmentGuideWelcomeBinding
import java.util.Locale


class WelcomeFragment : BaseFragment<GuideWelcomeViewModel, FragmentGuideWelcomeBinding>() {
    override fun onViewCreate() {
        val controller = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        binding.languageTitle.text = if (LanguageUtils.isAppliedLanguage()) {
            LanguageUtils.getAppliedLanguage().displayName
        } else Locale.getDefault().displayName
        binding.guideExit.setOnClickListener {
            if (!mViewModel.animIsRunning) {
                requireActivity().finish()
            }
        }
        binding.guideGetStart.setOnClickListener {
            if (!mViewModel.animIsRunning) {
                controller.navigate(R.id.permissionFragment, bundleOf(), navOptions {
                    anim {
                        enter = R.anim.guide_next_in
                        exit = R.anim.guide_next_out
                    }
                })
            }
        }
        languageAnimSet()
        binding.rv.linear().setup {
            addType<LanguageListModel> { R.layout.item_language_list }
        }.models = mutableListOf<Any>().apply {
            val tagList = arrayListOf(
                LanguageUtils.getSystemLanguage(),
                Locale.SIMPLIFIED_CHINESE,
                Locale.TAIWAN,
                Locale.ENGLISH,
                Locale.JAPANESE,
                Locale.GERMANY,
                Locale.FRANCE,
                Locale.forLanguageTag("ru")
            )
            tagList.forEachIndexed { _, s ->
                add(LanguageListModel(s, mViewModel))
            }
        }
    }

    private fun languageAnimSet() {
        mViewModel.startView = binding.localeChoose
        mViewModel.endView = binding.languageRoot
        mViewModel.rootView = binding.root
        mViewModel.maskView = binding.languageBackground
        mViewModel.duration = 650
        mViewModel.maskColor = Color.parseColor("#44000000")
        binding.localeChooseRoot.setOnClickListener {
            materialTransition(
                binding.localeChoose,
                binding.languageRoot,
                binding.root,
                binding.languageBackground,
                650,
                true,
                Color.parseColor("#44000000"),
                mViewModel
            )
        }
        binding.languageBackground.setOnClickListener {
            materialTransition(
                binding.languageRoot,
                binding.localeChoose,
                binding.root,
                binding.languageBackground,
                650,
                false,
                Color.parseColor("#44000000"),
                mViewModel
            )
        }
        binding.cancel.setOnClickListener {
            materialTransition(
                binding.languageRoot,
                binding.localeChoose,
                binding.root,
                binding.languageBackground,
                650,
                false,
                Color.parseColor("#44000000"),
                mViewModel
            )
        }
    }

    companion object {
        fun materialTransition(
            startView: View,
            endView: View,
            rootView: View,
            maskView: View,
            duration: Long,
            type: Boolean,
            maskColor: Int,
            mViewModel: GuideWelcomeViewModel
        ) {
            if (!mViewModel.animIsRunning) {
                mViewModel.animIsRunning = true
                MaterialContainerTransform().apply {
                    fadeMode = MaterialContainerTransform.FADE_MODE_CROSS
                    scrimColor = maskColor
                    maskView.setBackgroundColor(maskColor)
                    this.duration = duration
                    this.startView = startView
                    this.endView = endView
                    addTarget(endView)
                    addListener(object : TransitionListener {
                        override fun onTransitionStart(transition: androidx.transition.Transition) {
                            maskView.visibility = View.GONE
                        }

                        override fun onTransitionEnd(transition: androidx.transition.Transition) {
                            if (type) {
                                maskView.visibility = View.VISIBLE
                            }
                            if (mViewModel.isChoseLanguage) {
                                mViewModel.isChoseLanguage = false
                                LanguageUtils.applyLanguage(mViewModel.choseLocale)
                            }
                            mViewModel.animIsRunning = false
                        }

                        override fun onTransitionCancel(transition: androidx.transition.Transition) {}
                        override fun onTransitionPause(transition: androidx.transition.Transition) {}
                        override fun onTransitionResume(transition: androidx.transition.Transition) {}
                    })
                    TransitionManager.beginDelayedTransition((rootView as ViewGroup?)!!, this)
                    endView.visibility = View.VISIBLE
                    startView.visibility = View.INVISIBLE
                }
            }
        }
    }

    override fun getViewBinding(): FragmentGuideWelcomeBinding {
        return FragmentGuideWelcomeBinding.inflate(layoutInflater)
    }

}