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

object Note {
    /*
    * 数据库模式 更改于 2023年7月30日
    * val data = item.split("\u001A")
    * data[0] = 视频完整路径
    * data[1] = 视频Uri
    * data[2] = 视频被添加的时间戳
    * data[3] = 视频的标题
    * data[4] = 视频的大小
    * data[5] = 视频所属的上级文件夹
    * data[6] = 视频的时长
    * 获取完整文件夹代码：
    * path.substring(0, path!!.lastIndexOf("/") + 1)
    * */
}
