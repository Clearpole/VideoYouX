package com.videoyou.x.ui.fragment.guide

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
import com.videoyou.x.utils.base.BaseFragment
import com.videoyou.x.databinding.FragmentGuideWelcomeBinding
import com.videoyou.x.ui.fragment.guide.model.LanguageListModel
import java.util.Locale
import kotlin.properties.Delegates


class GuideWelcomeFragment : BaseFragment<FragmentGuideWelcomeBinding>() {
    private var animIsRunning = false
    private var startView by Delegates.notNull<View>()
    private var endView by Delegates.notNull<View>()
    private var rootView by Delegates.notNull<View>()
    private var maskView by Delegates.notNull<View>()
    private var duration by Delegates.notNull<Long>()
    private var maskColor by Delegates.notNull<Int>()
    var choseLocale by Delegates.notNull<Locale>()
    var isChoseLanguage = false
    override fun onViewCreate() {
        val controller = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)

        binding.languageTitle.text = if (LanguageUtils.isAppliedLanguage()) {
            LanguageUtils.getAppliedLanguage().displayName
        } else Locale.getDefault().displayName

        binding.guideExit.setOnClickListener {
            if (!animIsRunning) {
                requireActivity().finish()
            }
        }
        binding.guideGetStart.setOnClickListener {
            if (!animIsRunning) {
                controller.navigate(R.id.guidePermissionFragment, bundleOf(), navOptions {
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
                add(LanguageListModel(s))
            }
        }
    }

    private fun languageAnimSet() {
        startView = binding.localeChoose
        endView = binding.languageRoot
        rootView = binding.root
        maskView = binding.languageBackground
        duration = 650
        maskColor = Color.parseColor("#44000000")
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
    }

    fun materialTransition(
        startView: View,
        endView: View,
        rootView: View,
        maskView: View,
        duration: Long,
        type: Boolean,
        maskColor: Int
    ) {
        if (!animIsRunning) {
            animIsRunning = true
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
                        if (isChoseLanguage) {
                            isChoseLanguage = false
                            LanguageUtils.applyLanguage(choseLocale)
                        }
                        animIsRunning = false
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