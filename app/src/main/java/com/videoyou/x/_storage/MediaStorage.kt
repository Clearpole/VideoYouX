package com.videoyou.x._storage

import com.tencent.mmkv.MMKV

abstract class MediaStorage {
    open val kv_video : MMKV = MMKV.defaultMMKV()
}