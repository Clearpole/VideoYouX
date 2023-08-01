package com.videoyou.x.ui.guide.welcome

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
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
        binding.languageTitle.text = if (LanguageUtils.isAppliedLanguage()){LanguageUtils.getAppliedLanguage().displayName} else Locale.getDefault().displayName
        binding.localeChooseRoot.setOnClickListener {
            materialTransition(
                binding.localeChoose,
                binding.languageRoot,
                binding.root,
                binding.languageBackground,
                650,
                true,
                Color.parseColor("#44000000")
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
                Color.parseColor("#44000000")
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
                Color.parseColor("#44000000")
            )
        }
        binding.rv.linear().setup {
            addType<LanguageListModel> { R.layout.item_language_list }
        }.models = mutableListOf<Any>().apply {
            val tagList = arrayListOf(Locale.SIMPLIFIED_CHINESE,Locale.TAIWAN, Locale.ENGLISH,Locale.JAPANESE,Locale.GERMANY,Locale.FRANCE,Locale.forLanguageTag("ru"))
            tagList.forEachIndexed { index, s ->
                add(LanguageListModel(s,mViewModel))
            }
        }
    }

    private fun materialTransition(
        startView: View,
        endView: View,
        rootView: View,
        maskView: View,
        duration: Long,
        type:Boolean,
        maskColor: Int
    ) {
        if (!mViewModel.animIsRunning) {
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
                        mViewModel.animIsRunning = true
                        maskView.visibility = View.GONE
                    }

                    override fun onTransitionEnd(transition: androidx.transition.Transition) {
                        mViewModel.animIsRunning = false
                        if (type) {
                            maskView.visibility = View.VISIBLE
                        }
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

    override fun getViewBinding(): FragmentGuideWelcomeBinding {
        return FragmentGuideWelcomeBinding.inflate(layoutInflater)
    }

}