package com.clearpole.videoyoux

import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import androidx.activity.OnBackPressedCallback
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.TimeUtils
import com.clearpole.videoyoux._assembly.EmptyControlVideo
import com.clearpole.videoyoux._compose.GuideActivity
import com.clearpole.videoyoux._models.MainPageHomeModel
import com.clearpole.videoyoux._utils.ReadMediaStore
import com.clearpole.videoyoux._utils.RefreshMediaStore
import com.clearpole.videoyoux.databinding.ActivityMainBinding
import com.clearpole.videoyoux.databinding.ActivityMainLandBinding
import com.clearpole.videoyoux.screen_home.Greetings
import com.clearpole.videoyoux.screen_home.ViewPagerAdapter
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.drake.serialize.intent.openActivity
import com.drake.tooltip.toast
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.search.SearchBar
import com.google.android.material.search.SearchView
import com.google.android.material.search.SearchView.TransitionState
import com.gyf.immersionbar.ImmersionBar
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.cache.CacheFactory
import com.shuyu.gsyvideoplayer.cache.ProxyCacheManager
import com.shuyu.gsyvideoplayer.model.VideoOptionModel
import com.shuyu.gsyvideoplayer.player.IjkPlayerManager
import com.shuyu.gsyvideoplayer.player.PlayerFactory
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tv.danmaku.ijk.media.player.IjkMediaPlayer


class MainActivity :
    BaseActivity<ActivityMainBinding, ActivityMainLandBinding>(
        isHideStatus = false,
        isLandScape = false,
        ActivityMainBinding::inflate,
        ActivityMainLandBinding::inflate
    ) {
    private lateinit var videoPlayer: EmptyControlVideo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("values", MODE_PRIVATE)
        val isFirstEnter = sharedPreferences.getBoolean("first-enter", true)
        if (isFirstEnter) {
            openActivity<GuideActivity>()
            finish()
        } else {
           /* CoroutineScope(Dispatchers.IO).launch {
                refreshMediaData()
                cancel()
            }*/
            if (landScape) {
                startRailView()
                viewPagerLand()
            } else {
                bottomNavigationView()
                viewPager()
            }
            ConvertUtils.px2dp(
                ImmersionBar.getStatusBarHeight(this@MainActivity).toFloat() + 100
            ).apply {
                bindingLand.screenHomeRailSpace.layoutParams.width = this
                bindingLand.homeLandStatus.layoutParams.height = this - 100
            }
            videoPlayer()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val orientation = newConfig.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            viewPagerLand()
        } else {
            viewPager()
        }
    }

    private fun videoPlayer() {
        PlayerFactory.setPlayManager(IjkPlayerManager::class.java)
        IjkPlayerManager.setLogLevel(IjkMediaPlayer.IJK_LOG_SILENT)
        val list = arrayListOf(
            VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1),
            VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1),
            VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "enable-accurate-seek", 1),
            VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "packet-buffering", 0)
        )
        GSYVideoManager.instance().optionModelList = list
    }

    private fun startRailView() {
        /* bindingLand.screenHomeRail.apply {
             itemMinimumHeight = ScreenUtils.getScreenHeight() / 5
         }*/
    }

    private fun viewPagerLand() {
        val viewLand = bindingLand.screenHomeLandPagerView
        val pagesLandList = ArrayList<View>()
        pagesLandList.apply {
            add(View.inflate(this@MainActivity, R.layout.main_page_home_land, null))
            viewLand.adapter = ViewPagerAdapter(this)
            viewLand.setCanSwipe(false)
        }
        pagesLandList[0].apply {
            val rvLand = findViewById<RecyclerView>(R.id.home_land_rv)
            videoPlayer = findViewById(R.id.home_land_player)
            CoroutineScope(Dispatchers.IO).launch {
                val data = ReadMediaStore.readVideosData()
                val model = model(data, true)
                withContext(Dispatchers.Main) {
                    rvLand.linear().setup {
                        addType<MainPageHomeModel> { R.layout.main_page_rv_item_land }
                    }.models = model
                    rvLand.setHasFixedSize(true)
                }
            }
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
            val rv = findViewById<RecyclerView>(R.id.home_rv)
            logicList(rv)
            // wuhu
        }
    }

    private suspend fun refreshMediaData() {
        RefreshMediaStore.updateMedia(
            this@MainActivity,
            Environment.getExternalStorageDirectory().toString()
        )
        ReadMediaStore.writeData(contentResolver)
    }

    private fun logicList(rv: RecyclerView) {
        CoroutineScope(Dispatchers.IO).launch {
            val data = ReadMediaStore.readVideosData()
            val model = model(data, false)
            withContext(Dispatchers.Main) {
                rv.linear().setup {
                    it.layoutManager = CarouselLayoutManager()
                    addType<MainPageHomeModel> { R.layout.main_page_carousel_item }
                }.models = model
                rv.setHasFixedSize(true)
            }
        }
    }

    private fun model(dataList: MMKV, land: Boolean): MutableList<Any> {
        return mutableListOf<Any>().apply {
            val data = dataList.allKeys()!!.sortedBy { it.split("\u001A")[0] }.reversed()
            for (element in data) {
                val item = dataList.decodeString(element)!!.split("\u001A")
                val uri = item[1]
                val path = item[0]
                val title = element.split("\u001A")[1]
                if (land) {
                    add(MainPageHomeModel(path, uri, title, videoPlayer, true))
                } else {
                    add(MainPageHomeModel(path, uri, title, null, false))
                }
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
                        this.menu.getItem(3).icon =
                            getDrawableRes(R.drawable.outline_list_24)
                        this.menu.getItem(4).icon =
                            getDrawableRes(R.drawable.outline_settings_24)
                        true
                    }

                    R.id.menu_screen_page2 -> {
                        binding.screenHomePagerView.setCurrentItem(1, true)
                        it.icon = getDrawableRes(R.drawable.baseline_folder_24)
                        this.menu.getItem(0).icon =
                            getDrawableRes(R.drawable.outline_home_24)
                        this.menu.getItem(3).icon =
                            getDrawableRes(R.drawable.outline_list_24)
                        this.menu.getItem(4).icon =
                            getDrawableRes(R.drawable.outline_settings_24)
                        true
                    }

                    R.id.menu_screen_page4 -> {
                        binding.screenHomePagerView.setCurrentItem(2, true)
                        this.menu.getItem(0).icon =
                            getDrawableRes(R.drawable.outline_home_24)
                        this.menu.getItem(1).icon =
                            getDrawableRes(R.drawable.outline_folder_24)
                        this.menu.getItem(4).icon =
                            getDrawableRes(R.drawable.outline_settings_24)
                        true
                    }

                    R.id.menu_screen_page5 -> {
                        binding.screenHomePagerView.setCurrentItem(4, true)
                        it.icon = getDrawableRes(R.drawable.baseline_settings_24)
                        this.menu.getItem(0).icon =
                            getDrawableRes(R.drawable.outline_home_24)
                        this.menu.getItem(1).icon =
                            getDrawableRes(R.drawable.outline_folder_24)
                        this.menu.getItem(3).icon =
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