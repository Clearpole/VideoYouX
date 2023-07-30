package com.clearpole.videoyoux

import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.TimeUtils
import com.clearpole.videoyoux._adapter.ViewPagerAdapter
import com.clearpole.videoyoux._compose.DevelopActivity
import com.clearpole.videoyoux._models.MainPageHomeModel
import com.clearpole.videoyoux._utils.ReadMediaStore
import com.clearpole.videoyoux._utils.RefreshMediaStore
import com.clearpole.videoyoux.databinding.ActivityMainBinding
import com.clearpole.videoyoux.screen_home.Greetings
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.drake.serialize.intent.openActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.carousel.CarouselLayoutManager
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity :
    BaseActivity<ActivityMainBinding>(
        isHideStatus = false,
        inflate = ActivityMainBinding::inflate,
    ) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("values", MODE_PRIVATE)
        val isFirstEnter = sharedPreferences.getBoolean("first-enter", true)
        if (isFirstEnter) {
            openActivity<GuideActivity>()
            finish()
        } else {
            bottomNavigationView()
            viewPager()
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
            homeTitleAnim(findViewById(R.id.titleRoot), findViewById(R.id.toolbarRoot))
        }
    }

    private fun homeTitleAnim(titleRoot: View, toolBar: View) {
        val disAppear = AnimationUtils.loadAnimation(this, R.anim.disappear_appear)
        val appearDis = AnimationUtils.loadAnimation(this, R.anim.appear_disappear)
        CoroutineScope(Dispatchers.IO).launch {
            delay(600)
            withContext(Dispatchers.Main) {
                titleRoot.visibility = View.VISIBLE
                titleRoot.startAnimation(disAppear)
            }
            delay(600)
            withContext(Dispatchers.Main) {
                titleRoot.startAnimation(appearDis)
                titleRoot.visibility = View.GONE
            }
            delay(600)
            withContext(Dispatchers.Main) {
                toolBar.startAnimation(disAppear)
                toolBar.visibility = View.VISIBLE
                toolBar.setOnClickListener {
                    openActivity<DevelopActivity>()
                }
            }
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
            refreshMediaData().apply {
                val data = ReadMediaStore.readVideosData()
                val model = model(data)
                withContext(Dispatchers.Main) {
                    rv.linear().setup {
                        it.layoutManager = CarouselLayoutManager()
                        addType<MainPageHomeModel> { R.layout.item_main_carousel }
                    }.models = model
                    rv.setHasFixedSize(true)
                }
            }
        }
    }

    private fun model(dataList: MMKV): MutableList<Any> {
        return mutableListOf<Any>().apply {
            val data = dataList.allKeys()!!.sortedBy { it.split("\u001A")[0] }.reversed()
            for (element in data) {
                val item = dataList.decodeString(element)!!.split("\u001A")
                val uri = item[1]
                val path = item[0]
                val title = element.split("\u001A")[1]
                add(MainPageHomeModel(path, Uri.parse(uri), title, false))
            }
        }
    }

    private fun bottomNavigationView() {
        binding.screenHomeBottomView.apply {
            this!!.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.menu_screen_page1 -> {
                        binding.screenHomePagerView!!.setCurrentItem(0, true)
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
                        binding.screenHomePagerView!!.setCurrentItem(1, true)
                        it.icon = getDrawableRes(R.drawable.baseline_folder_24)
                        this.menu.getItem(0).icon =
                            getDrawableRes(R.drawable.outline_home_24)
                        this.menu.getItem(3).icon =
                            getDrawableRes(R.drawable.outline_list_24)
                        this.menu.getItem(4).icon =
                            getDrawableRes(R.drawable.outline_settings_24)
                        true
                    }

                    R.id.menu_screen_page3 -> {
                        true
                    }

                    R.id.menu_screen_page4 -> {
                        binding.screenHomePagerView!!.setCurrentItem(2, true)
                        this.menu.getItem(0).icon =
                            getDrawableRes(R.drawable.outline_home_24)
                        this.menu.getItem(1).icon =
                            getDrawableRes(R.drawable.outline_folder_24)
                        this.menu.getItem(4).icon =
                            getDrawableRes(R.drawable.outline_settings_24)
                        true
                    }

                    R.id.menu_screen_page5 -> {
                        binding.screenHomePagerView!!!!.setCurrentItem(4, true)
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