package com.videoyou.x._utils

import android.content.Context
import android.os.Bundle
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator


@Navigator.Name("customNavigator")
class CustomNavigator(
    private val context: Context, private val fragmentManager: FragmentManager,
    private val containerId: Int
) :
    FragmentNavigator(context, fragmentManager, containerId) {

    override fun navigate(
        destination: Destination,
        args: Bundle?,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ): NavDestination? {
        val ft: FragmentTransaction = fragmentManager.beginTransaction()
        // 获取当前显示的Fragment
        var fragment: Fragment? = fragmentManager.getPrimaryNavigationFragment()
        if (fragment != null) {
            ft.hide(fragment)
        }

        val tag = destination.id.toString()
        fragment = fragmentManager.findFragmentByTag(tag)!!
        ft.show(fragment)
        ft.setPrimaryNavigationFragment(fragment)
        ft.setReorderingAllowed(true)
        ft.commit()
        return destination
    }
}

