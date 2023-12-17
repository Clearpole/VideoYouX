package com.videoyou.x._storage

import com.tencent.mmkv.MMKV

class Statistics {
    companion object {
        const val FOLDERS_COUNT = "folders_count"
        const val VIDEOS_COUNT = "videos_count"
        const val PLAY_COUNT = "videos_count"

        private val kv_statistics = MMKV.mmkvWithID("vyx-statistics", MMKV.SINGLE_PROCESS_MODE)!!
        fun info(): MMKV = kv_statistics

        fun writeInfo(type: String, value: String) {
            kv_statistics.encode(type, value)
        }

        fun readInfo(type: String): String {
            return kv_statistics.decodeString(type).let { if (it.isNullOrEmpty()) "0" else it }
        }
    }
}