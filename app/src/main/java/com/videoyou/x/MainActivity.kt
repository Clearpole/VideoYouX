package com.videoyou.x

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.drake.serialize.intent.openActivity
import com.videoyou.x._storage.AndroidMediaStore
import com.videoyou.x._utils.base.BaseActivity
import com.videoyou.x.databinding.ActivityMainBinding
import com.videoyou.x.ui.fragment.files.FilesFragment
import com.videoyou.x.ui.fragment.home.HomeFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : BaseActivity<ActivityMainBinding>(
) {
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

        val homeFragment = HomeFragment()
        val filesFragment = FilesFragment()

        supportFragmentManager.commit {
            add(R.id.nav_host_fragment, homeFragment)
            add(R.id.nav_host_fragment,filesFragment)
            hide(filesFragment)
        }

        binding.navView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_screen_page1 -> {
                    supportFragmentManager.commit {
                        show(homeFragment)
                        hide(filesFragment)
                    }
                    true
                }

                R.id.menu_screen_page2 -> {
                    supportFragmentManager.commit {
                        show(filesFragment)
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

    override fun getLayout(): Int {
        return R.layout.activity_main
    }
}