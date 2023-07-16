package com.clearpole.videoyoux

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.clearpole.videoyoux._adapter.ViewPagerAdapter
import com.clearpole.videoyoux._assembly.FixedSpeedScroller
import com.clearpole.videoyoux._models.GuidePermissionModel
import com.clearpole.videoyoux.databinding.ActivityGuideBinding
import com.clearpole.videoyoux.databinding.ActivityGuideLandBinding
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.reflect.Field


class GuideActivity : BaseActivity<ActivityGuideBinding, ActivityGuideLandBinding>(
    isHideStatus = false,
    isLandScape = false,
    isRequireLightBarText = false,
    inflate = ActivityGuideBinding::inflate,
    inflateLand = ActivityGuideLandBinding::inflate
) {
    private lateinit var pageList: ArrayList<View>
    private var mScroller: FixedSpeedScroller? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewPager()
    }


    private fun viewPager() {
        val view = binding.guidePagerView
        val pagesList = ArrayList<View>()
        pagesList.apply {
            add(View.inflate(this@GuideActivity, R.layout.guide_welcome, null))
            add(View.inflate(this@GuideActivity, R.layout.guide_permission, null))
            add(View.inflate(this@GuideActivity, R.layout.guide_data_store, null))
            add(View.inflate(this@GuideActivity, R.layout.guide_settings, null))
            view.adapter = ViewPagerAdapter(this)
            controlViewPagerSpeed(this@GuideActivity, view, 250)
            view.setCanSwipe(false)
            pageList = this
        }.let {
            it[0].apply {
                findViewById<MaterialButton>(R.id.guide_getStart).setOnClickListener {
                    viewPagerAnimation(view, false)
                    binding.guidePagerView.setCurrentItem(1, true)
                }
                findViewById<MaterialTextView>(R.id.guide_exit).setOnClickListener {
                    finish()
                }
            }
            it[1].apply {
                findViewById<MaterialTextView>(R.id.guide_permission_back).setOnClickListener {
                    binding.guidePagerView.setCurrentItem(0, true)
                }
                findViewById<RecyclerView>(R.id.guide_permission_list).linear().setup {
                    addType<GuidePermissionModel> { R.layout.guide_permission_list }
                }.models = mutableListOf<Any?>().apply {
                    add(GuidePermissionModel(Permissions.storage,
                        context.getString(R.string.read_video_permission),
                        context.getString(R.string.permission_use_storage),
                        AppCompatResources.getDrawable(this@GuideActivity,R.drawable.outline_video_file_24)
                    ))
                }
            }
        }
    }

    private fun viewPagerAnimation(view: View, back: Boolean) {
        AlphaAnimation(1f, 0f).apply {
            duration = 150
            view.startAnimation(this)
        }.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(p0: Animation?) {}
            override fun onAnimationEnd(p0: Animation?) {
                view.visibility = View.GONE
                CoroutineScope(Dispatchers.IO).launch {
                    delay(100)
                    withContext(Dispatchers.Main) {
                        view.visibility = View.VISIBLE
                        AlphaAnimation(0f, 1f).apply {
                            duration = 150
                            view.startAnimation(this)
                        }
                    }
                }
            }

            override fun onAnimationRepeat(p0: Animation?) {}
        })
    }

    private fun controlViewPagerSpeed(
        context: Context?,
        viewpager: ViewPager?,
        DurationSwitch: Int
    ) {
        try {
            val mField: Field = ViewPager::class.java.getDeclaredField("mScroller")
            mField.isAccessible = true
            mScroller = FixedSpeedScroller(
                context,
                AccelerateInterpolator()
            )
            mScroller!!.setmDuration(DurationSwitch)
            mField.set(viewpager, mScroller)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}