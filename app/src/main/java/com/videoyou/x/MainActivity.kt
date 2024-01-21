package com.videoyou.x

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.commit
import com.drake.serialize.intent.openActivity
import com.videoyou.x.storage.AndroidMediaStore
import com.videoyou.x.utils.base.BaseActivity
import com.videoyou.x.databinding.ActivityMainBinding
import com.videoyou.x.ui.fragment.functions.FunctionsFragment
import com.videoyou.x.ui.fragment.home.HomeFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : BaseActivity<ActivityMainBinding>(
) {
    private var firstLoad = false
    private val homeFragment = HomeFragment()
    private val functionsFragment = FunctionsFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("values", MODE_PRIVATE)
        val isFirstEnter = sharedPreferences.getBoolean("first-enter", true)
        if (isFirstEnter) {
            openActivity<GuideActivity>()
        }
        CoroutineScope(Dispatchers.IO).launch {
            AndroidMediaStore.writeData(this@MainActivity, false)
        }

        binding.navView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_screen_page1 -> {
                    supportFragmentManager.commit {
                        setCustomAnimations(R.anim.fade_in,R.anim.fade_out)
                        show(homeFragment)
                        hide(functionsFragment)
                    }
                    true
                }

                R.id.menu_screen_page2 -> {
                    supportFragmentManager.commit {
                        setCustomAnimations(R.anim.fade_in,R.anim.fade_out)
                        show(functionsFragment)
                        hide(homeFragment)
                    }
                    true
                }

                else -> {
                    true
                }
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (!firstLoad) {
            supportFragmentManager.commit {
                add(R.id.nav_host_fragment, homeFragment)
                add(R.id.nav_host_fragment, functionsFragment)
                hide(functionsFragment)
            }
            firstLoad = true
        }
    }

    override fun getLayout(): Int {
        return R.layout.activity_main
    }
}