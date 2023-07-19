package com.clearpole.videoyoux

import com.hjq.permissions.Permission

object Develop {
    const val TAG = "Vyx"
}

object Values {
    var isSystem = false
    const val KEY = "vyx-encode-key"
}

object Vars {
    var guideRequireAnim = true
}

object Permissions {
    const val storage = Permission.MANAGE_EXTERNAL_STORAGE
    const val video = Permission.READ_MEDIA_VIDEO
}
