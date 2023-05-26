package com.clearpole.videoyoux

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import androidx.activity.OnBackPressedCallback
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.TimeUtils
import com.clearpole.videoyoux.compose.ui.GuideActivity
import com.clearpole.videoyoux.databinding.ActivityMainBinding
import com.clearpole.videoyoux.databinding.ActivityMainLandBinding
import com.clearpole.videoyoux.models.CarouselModel
import com.clearpole.videoyoux.screen_home.Greetings
import com.clearpole.videoyoux.screen_home.ViewPagerAdapter
import com.clearpole.videoyoux.utils.ReadMediaStore
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.drake.serialize.intent.openActivity
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.search.SearchBar
import com.google.android.material.search.SearchView
import com.google.android.material.search.SearchView.TransitionState
import com.gyf.immersionbar.ImmersionBar
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity :
    BaseActivity<ActivityMainBinding, ActivityMainLandBinding>(
        isHideStatus = false,
        isLandScape = false,
        ActivityMainBinding::inflate,
        ActivityMainLandBinding::inflate
    ) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("values", MODE_PRIVATE)
        val isFirstEnter = sharedPreferences.getBoolean("first-enter", true)
        if (isFirstEnter) {
            openActivity<GuideActivity>()
            finish()
        } else {
            if (landScape) {
                startRailView()
            } else {
                bottomNavigationView()
                viewPager()
            }
        }
    }

    private fun startRailView() {
        bindingLand.screenHomeRail.apply {
            bindingLand.screenHomeRailSpace.layoutParams.width = ConvertUtils.px2dp(
                ImmersionBar.getStatusBarHeight(this@MainActivity).toFloat()
            )
            itemMinimumHeight = ScreenUtils.getScreenHeight() / 5
        }
    }

    private fun viewPager() {
        val view = binding.screenHomePagerView
        val pagesList = ArrayList<View>()
        pagesList.apply {
            add(View.inflate(this@MainActivity, R.layout.main_page_home, null))
            add(View.inflate(this@MainActivity, R.layout.main_page_folders, null))
            add(View.inflate(this@MainActivity, R.layout.main_page_play, null))
            add(View.inflate(this@MainActivity, R.layout.main_page_settings, null))
            view.adapter = ViewPagerAdapter(this)
            view.setCanSwipe(false)
        }

        pagesList[0].apply {
            val animUpDown = AnimationUtils.loadAnimation(
                this@MainActivity,
                R.anim.main_up_down
            )
            animUpDown.interpolator = AccelerateDecelerateInterpolator()
            val animDownUp = AnimationUtils.loadAnimation(this@MainActivity, R.anim.main_down_up)
            animDownUp.interpolator = AccelerateDecelerateInterpolator()
            val searchView = findViewById<SearchView>(R.id.cat_search_view)
            val greetings = greetings()
            searchView.hint = greetings
            findViewById<SearchBar>(R.id.page_home_search_bar).hint = greetings
            searchView.isAutoShowKeyboard = false
            searchView.addTransitionListener { _, _, newState ->
                when (newState) {
                    TransitionState.SHOWING -> {
                        binding.screenHomeBottomView.visibility = View.GONE
                        binding.screenHomeBottomView.startAnimation(
                            animUpDown
                        )
                    }

                    TransitionState.HIDDEN -> {
                        binding.screenHomeBottomView.startAnimation(
                            animDownUp
                        )
                        binding.screenHomeBottomView.visibility = View.VISIBLE
                    }

                    else -> {}
                }
            }
            val onBackPressedCallback: OnBackPressedCallback =
                object : OnBackPressedCallback(false) {
                    override fun handleOnBackPressed() {
                        searchView.hide()
                    }
                }
            this@MainActivity.onBackPressedDispatcher.addCallback(
                this@MainActivity,
                onBackPressedCallback
            )
            searchView.addTransitionListener { _: SearchView?, _: TransitionState?, newState: TransitionState ->
                onBackPressedCallback.isEnabled =
                    newState == TransitionState.SHOWN
            }
            val rv = findViewById<RecyclerView>(R.id.home_rv)
            CoroutineScope(Dispatchers.IO).launch {
                val data = ReadMediaStore.readVideosData()
                val model = carouselModel(data)
                withContext(Dispatchers.Main) {
                    rv.linear().setup {
                        it.layoutManager = CarouselLayoutManager()
                        addType<CarouselModel> { R.layout.main_page_carousel_item }
                    }.models = model
                }
            }
        }
    }

    private fun carouselModel(dataList: MMKV): MutableList<Any> {
        return mutableListOf<Any>().apply {
            val data = dataList.allKeys()!!.sortedBy { it.split("\u001A")[0] }.reversed()
            for (element in data) {
                val item = dataList.decodeString(element)!!.split("\u001A")[1]
                add(CarouselModel(item))
            }
        }
    }

    private fun bottomNavigationView() {
        binding.screenHomeBottomView.apply {
            this.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.menu_screen_page1 -> {
                        binding.screenHomePagerView.setCurrentItem(0, true)
                        it.icon = getDrawableRes(R.drawable.baseline_home_24)
                        this.menu.getItem(1).icon =
                            getDrawableRes(R.drawable.outline_folder_24)
                        this.menu.getItem(2).icon =
                            getDrawableRes(R.drawable.outline_list_24)
                        this.menu.getItem(3).icon =
                            getDrawableRes(R.drawable.outline_settings_24)
                        true
                    }

                    R.id.menu_screen_page2 -> {
                        binding.screenHomePagerView.setCurrentItem(1, true)
                        it.icon = getDrawableRes(R.drawable.baseline_folder_24)
                        this.menu.getItem(0).icon =
                            getDrawableRes(R.drawable.outline_home_24)
                        this.menu.getItem(2).icon =
                            getDrawableRes(R.drawable.outline_list_24)
                        this.menu.getItem(3).icon =
                            getDrawableRes(R.drawable.outline_settings_24)
                        true
                    }

                    R.id.menu_screen_page3 -> {
                        binding.screenHomePagerView.setCurrentItem(2, true)
                        this.menu.getItem(0).icon =
                            getDrawableRes(R.drawable.outline_home_24)
                        this.menu.getItem(1).icon =
                            getDrawableRes(R.drawable.outline_folder_24)
                        this.menu.getItem(3).icon =
                            getDrawableRes(R.drawable.outline_settings_24)
                        true
                    }

                    R.id.menu_screen_page4 -> {
                        binding.screenHomePagerView.setCurrentItem(3, true)
                        it.icon = getDrawableRes(R.drawable.baseline_settings_24)
                        this.menu.getItem(0).icon =
                            getDrawableRes(R.drawable.outline_home_24)
                        this.menu.getItem(1).icon =
                            getDrawableRes(R.drawable.outline_folder_24)
                        this.menu.getItem(2).icon =
                            getDrawableRes(R.drawable.outline_list_24)
                        true
                    }

                    else -> {
                        false
                    }
                }
            }
        }
    }

    private fun greetings(): String {
        val sharedPreferences = getSharedPreferences("values", MODE_PRIVATE)
        return if (TimeUtils.getChineseWeek(TimeUtils.getNowDate())
                .equals(sharedPreferences.getString("chinese-week", "null"))
        ) {
            sharedPreferences.getString("greetings-text", "美好的一天")!!
        } else {
            val editor = sharedPreferences.edit()
            val greetings = Greetings.text()
            editor.putString("chinese-week", TimeUtils.getChineseWeek(TimeUtils.getNowDate()))
            editor.putString("greetings-text", greetings)
            editor.apply()
            greetings
        }
    }

    private fun getDrawableRes(id: Int): Drawable {
        return ResourcesCompat.getDrawable(resources, id, null)!!
    }
}