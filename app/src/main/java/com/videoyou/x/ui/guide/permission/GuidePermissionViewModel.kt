package com.videoyou.x.ui.guide.permission

import androidx.lifecycle.ViewModel
import kotlin.properties.Delegates

class GuidePermissionViewModel : ViewModel() {
    var test by Delegates.notNull<String>()
}