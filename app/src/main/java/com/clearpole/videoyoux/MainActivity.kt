package com.clearpole.videoyoux

import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.TimeUtils
import com.clearpole.videoyoux.databinding.ActivityMainBinding
import com.clearpole.videoyoux.screen_home.Greetings
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.gyf.immersionbar.ImmersionBar


class MainActivity : BaseActivity<ActivityMainBinding>(isHideStatus = false) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        landscapeOrOrientation(resources.configuration.orientation)
        bottomNavigationView(binding.screenHomeBottomView)
        binding.screenHomeRailSpace.layoutParams.width = ConvertUtils.px2dp(
            ImmersionBar.getStatusBarHeight(this).toFloat()
        )
        binding.screenHomeRail.itemMinimumHeight = ScreenUtils.getScreenHeight()/5
        println(ImmersionBar.getStatusBarHeight(this))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        landscapeOrOrientation(newConfig.orientation)
    }

    private fun landscapeOrOrientation(orientation: Int) {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.screenHomeBottomView.visibility = View.GONE
            binding.screenHomeRail.visibility = View.VISIBLE
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            binding.screenHomeBottomView.visibility = View.VISIBLE
            binding.screenHomeRail.visibility = View.GONE
        }
    }

    private fun bottomNavigationView(view: BottomNavigationView) {
        view.apply {
            this.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.menu_screen_page1 -> {
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
                        this.menu.getItem(0).icon =
                            getDrawableRes(R.drawable.outline_home_24)
                        this.menu.getItem(1).icon =
                            getDrawableRes(R.drawable.outline_folder_24)
                        this.menu.getItem(3).icon =
                            getDrawableRes(R.drawable.outline_settings_24)
                        true
                    }

                    R.id.menu_screen_page4 -> {
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