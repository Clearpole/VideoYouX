package com.clearpole.videoyoux

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import androidx.viewpager.widget.ViewPager
import com.clearpole.videoyoux._adapter.ViewPagerAdapter
import com.clearpole.videoyoux._assembly.FixedSpeedScroller
import com.clearpole.videoyoux.databinding.ActivityGuideBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.reflect.Field

class GuideActivity : BaseActivity<ActivityGuideBinding>(
    isHideStatus = false,
    isLandScape = false,
    isRequireLightBarText = false,
    inflate = ActivityGuideBinding::inflate
) {
    private lateinit var pageList: ArrayList<View>
    private var mScroller: FixedSpeedScroller? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewPager(landScape)
    }


    private fun viewPager(mLandSpace: Boolean) {
        val view = binding.guidePagerView
        val pagesList = ArrayList<View>()
        pagesList.apply {
            add(
                View.inflate(
                    this@GuideActivity,
                    if (!mLandSpace) R.layout.guide_welcome else R.layout.guide_welcome,
                    null
                )
            )
            add(
                View.inflate(
                    this@GuideActivity,
                    if (!mLandSpace) R.layout.guide_permission else R.layout.guide_permission,
                    null
                )
            )
            add(
                View.inflate(
                    this@GuideActivity,
                    if (!mLandSpace) R.layout.guide_data_store else R.layout.guide_data_store,
                    null
                )
            )
            add(
                View.inflate(
                    this@GuideActivity,
                    if (!mLandSpace) R.layout.guide_settings else R.layout.guide_settings,
                    null
                )
            )
            view.adapter = ViewPagerAdapter(this)
            controlViewPagerSpeed(this@GuideActivity, view, 250)
            view.setCanSwipe(false)
            pageList = this
        }.let {
           /* it[0].apply {
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
                    viewPagerAnimation(view, false)
                    binding.guidePagerView.setCurrentItem(0, true)
                }
                val recyclerView = findViewById<RecyclerView>(R.id.guide_permission_list)
                recyclerView.linear().setup {
                    addType<GuidePermissionModel> { R.layout.item_guide_permission }
                }.models = mutableListOf<Any?>().apply {
                    add(
                        GuidePermissionModel(
                            Permissions.video,
                            context.getString(R.string.read_video_permission),
                            context.getString(R.string.permission_use_storage),
                            AppCompatResources.getDrawable(
                                this@GuideActivity,
                                R.drawable.outline_video_file_24
                            )
                        )
                    )
                }
                findViewById<MaterialButton>(R.id.guide_permission_next).setOnClickListener {
                    if (XXPermissions.isGranted(this@GuideActivity, Permissions.video)) {
                        viewPagerAnimation(view, false)
                        binding.guidePagerView.setCurrentItem(2, true)
                    } else {
                        val anim =
                            ObjectAnimator.ofFloat(recyclerView, View.TRANSLATION_X.name, 0f, 8f)
                        anim.setDuration(300)
                        anim.interpolator = CycleInterpolator(4f)
                        anim.start()
                    }
                }
            }
            it[2].apply {
                findViewById<MaterialTextView>(R.id.guide_data_store_back).setOnClickListener {
                    viewPagerAnimation(view, false)
                    binding.guidePagerView.setCurrentItem(1, true)
                }
                findViewById<MaterialButton>(R.id.guide_data_store_next).setOnClickListener {
                    if ((it as MaterialButton).text == getString(R.string.start_read)) {
                        val progressView =
                            findViewById<CircularProgressIndicator>(R.id.guide_data_store_progress)
                        progressView.visibility = View.VISIBLE
                        it.text = getString(R.string.reading)
                        CoroutineScope(Dispatchers.IO).launch {
                            ReadMediaStore.writeData(contentResolver).apply {
                                delay(1000)
                                withContext(Dispatchers.Main) {
                                    progressView.visibility = View.GONE
                                    it.text = getString(R.string.go_to_home)
                                }
                            }
                        }
                    } else if (it.text == getString(R.string.go_to_home)) {
                        val sharedPreferences =
                            getSharedPreferences("values", MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putBoolean("first-enter", false)
                        editor.apply()
                        openActivity<MainActivity>()
                        finish()
                    }
                }
            }*/
        }
    }

    private fun viewPagerAnimation(view: View, back: Boolean) {
        AlphaAnimation(1f, 0f).apply {
            duration = if (back) 10 else 150
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
        duration: Int
    ) {
        try {
            val mField: Field = ViewPager::class.java.getDeclaredField("mScroller")
            mField.isAccessible = true
            mScroller = FixedSpeedScroller(
                context,
                AccelerateInterpolator()
            )
            mScroller!!.setmDuration(duration)
            mField.set(viewpager, mScroller)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}