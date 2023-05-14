package com.clearpole.videoyoux

import com.hjq.permissions.Permission

object Values {
    const val KEY = "vyx-encode-key"
    var FOLDER_ALL_LOAD: Boolean = true
}

object NavHostIn {
    const val NAV_HOME = "home"
    const val NAV_FOLDER = "folder"
    const val NAV_FOLDER_ALL = "folder-all"
    const val NAV_GUIDE_WELCOME = "welcome"
    const val NAV_GUIDE_PERMISSION = "permission"
    const val NAV_GUIDE_WRITE_DATA = "writeData"
}

object Permissions {
    val PERMISSIONS = mutableListOf(Permission.READ_MEDIA_VIDEO)
}