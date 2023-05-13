package com.clearpole.videoyoux.utils

import android.content.ContentResolver
import android.net.Uri
import android.provider.MediaStore
import com.clearpole.videoyoux.Values
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject


class ReadMediaStore {
    companion object {
        private val kv_video = MMKV.mmkvWithID("vyx-videos", MMKV.SINGLE_PROCESS_MODE, Values.KEY)!!
        suspend fun readFolder(): ArrayList<String> = withContext(Dispatchers.IO) {
            val array = arrayListOf<String>()
            kv_video.allKeys()!!.toMutableList().forEach {
                array.add(it)
            }
            array
        }

        suspend fun readVideos(folder: String): JSONArray = JSONArray(
            kv_video.decodeString(folder)
        )

        suspend fun writeData(contentResolver: ContentResolver) {
            kv_video.clearAll()
            var itemArray = JSONArray()
            withContext(Dispatchers.IO) {
                contentResolver.query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, null
                )!!.apply {
                    moveToPosition(-1)
                    while (moveToNext()) {
                        val itemJson = JSONObject()
                        val folder =
                            getString(getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME))
                                ?: "根目录"
                        itemJson.put(
                            "title", getString(getColumnIndexOrThrow(MediaStore.Video.Media.TITLE))
                        )
                        itemJson.put(
                            "size", getString(getColumnIndexOrThrow(MediaStore.Video.Media.SIZE))
                        )
                        itemJson.put(
                            "path", getString(getColumnIndexOrThrow(MediaStore.Video.Media.DATA))
                        )
                        itemJson.put(
                            "uri", Uri.withAppendedPath(
                                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                                getString(getColumnIndexOrThrow(MediaStore.Video.Media._ID))
                            )
                        )
                        itemJson.put(
                            "folder",
                            getString(getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME))
                        )
                        itemJson.put(
                            "duration",
                            getString(getColumnIndexOrThrow(MediaStore.Video.Media.DURATION))
                        )
                        itemJson.put(
                            "dateAdded",
                            getString(getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED))
                        )
                        itemArray.put(itemJson)
                        if (kv_video.containsKey(folder)) {
                            val array = JSONArray(kv_video.decodeString(folder))
                            kv_video.remove(folder)
                            for (index in 0..array.length()) {
                                try {
                                    itemArray.put(array[index])
                                } catch (_: Exception) {
                                }
                            }
                            kv_video.putString(folder, itemArray.toString())
                        } else {
                            kv_video.encode(folder, itemArray.toString())
                        }
                        itemArray = JSONArray()
                    }
                    close()
                }
            }
        }
    }
}