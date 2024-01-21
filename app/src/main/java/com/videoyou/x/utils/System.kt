package com.videoyou.x.utils

import android.content.res.Configuration
import android.content.res.Resources

object System {
    fun isNightMode(resources: Resources): Boolean {
        return when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            else -> false
        }
    }
}