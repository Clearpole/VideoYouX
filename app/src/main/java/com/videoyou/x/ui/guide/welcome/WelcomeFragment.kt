package com.videoyou.x.ui.guide.welcome

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.transition.Transition.TransitionListener
import androidx.transition.TransitionManager
import com.google.android.material.transition.MaterialContainerTransform
import com.videoyou.x._utils.BaseFragment
import com.videoyou.x.databinding.FragmentGuideWelcomeBinding


class WelcomeFragment : BaseFragment<GuideWelcomeViewModel, FragmentGuideWelcomeBinding>() {
    override fun onViewCreate() {
        binding.languageTitle.text =
            requireActivity().applicationContext.resources.configuration.locales.toLanguageTags()
        binding.localeChoose.setOnClickListener {
            materialTransition(
                binding.localeChoose,
                binding.languageRoot,
                binding.root,
                binding.languageBackground,
                800
            )
        }
        binding.languageBackground.setOnClickListener {
            materialTransition(
                binding.languageRoot,
                binding.localeChoose,
                binding.root,
                binding.languageBackground,
                800
            )
        }
    }

    private fun materialTransition(
        startView: View,
        endView: View,
        rootView: View,
        maskView: View,
        duration: Long
    ) {
        MaterialContainerTransform().apply {
            fadeMode = MaterialContainerTransform.FADE_MODE_CROSS
            scrimColor = Color.parseColor("#99444444")
            this.duration = duration
            this.startView = startView
            this.endView = endView
            addTarget(endView)
            TransitionManager.beginDelayedTransition((rootView as ViewGroup?)!!, this)
            endView.visibility = View.VISIBLE
            startView.visibility = View.INVISIBLE
            addListener(object : TransitionListener {
                override fun onTransitionStart(transition: androidx.transition.Transition) {
                    TODO("Not yet implemented")
                }

                override fun onTransitionEnd(transition: androidx.transition.Transition) {
                    TODO("Not yet implemented")
                }

                override fun onTransitionCancel(transition: androidx.transition.Transition) {
                    TODO("Not yet implemented")
                }

                override fun onTransitionPause(transition: androidx.transition.Transition) {
                    TODO("Not yet implemented")
                }

                override fun onTransitionResume(transition: androidx.transition.Transition) {
                    TODO("Not yet implemented")
                }


            })
            /* ObjectAnimator.ofFloat(binding.languageBackground,"alpha",1f,0f).apply {
             start()
             doOnEnd {
                 binding.languageBackground.visibility = View.GONE
             }
         }*/
        }
    }

    override fun getViewBinding(): FragmentGuideWelcomeBinding {
        return FragmentGuideWelcomeBinding.inflate(layoutInflater)
    }

}