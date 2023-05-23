package com.clearpole.videoyoux.utils

import android.content.ContentResolver
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.clearpole.videoyoux.Values
import com.drake.tooltip.toast
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject


class ReadMediaStore {
    companion object {
        private val kv_video = MMKV.mmkvWithID("vyx-videos", MMKV.SINGLE_PROCESS_MODE)!!
        suspend fun readVideosData(): MMKV = kv_video

        suspend fun writeData(contentResolver: ContentResolver) {
            kv_video.clearAll()
            withContext(Dispatchers.IO) {
                contentResolver.query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, null
                )!!.apply {
                    moveToPosition(-1)
                    while (moveToNext()) {
                        val timeStamp =
                            getString(getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED))
                        val title = getString(getColumnIndexOrThrow(MediaStore.Video.Media.TITLE))
                        val size = getString(getColumnIndexOrThrow(MediaStore.Video.Media.SIZE))
                        val folder =
                            getString(getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME))
                        val path = getString(getColumnIndexOrThrow(MediaStore.Video.Media.DATA))
                        val duration =
                            getString(getColumnIndexOrThrow(MediaStore.Video.Media.DURATION))
                        val uri = Uri.withAppendedPath(
                            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                            getString(getColumnIndexOrThrow(MediaStore.Video.Media._ID))
                        )
                        val key = "$timeStamp\u001a$title\u001A$size\u001A$folder\u001A$duration"
                        kv_video.encode(key, "$path\u001A$uri")
                    }
                    close()
                }
            }
        }
    }
}