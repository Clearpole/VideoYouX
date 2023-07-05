package com.clearpole.videoyoux

import com.hjq.permissions.Permission

object Develop {
    const val TAG = "Vyx"
}

object Values {
    var isSystem = false
    const val KEY = "vyx-encode-key"
}

object Permissions {
    val PERMISSIONS = mutableListOf(Permission.READ_MEDIA_VIDEO)
}