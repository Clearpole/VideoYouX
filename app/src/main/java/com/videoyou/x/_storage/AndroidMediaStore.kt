package com.videoyou.x._storage

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import com.bumptech.glide.Glide
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.system.measureNanoTime


class AndroidMediaStore {
    companion object {
        private val kv_video = MMKV.mmkvWithID("vyx-videos", MMKV.SINGLE_PROCESS_MODE)!!
        private val kv_folder_time = MMKV.mmkvWithID("vyx-folders", MMKV.SINGLE_PROCESS_MODE)!!
        fun readVideosData(): MMKV = kv_video
        fun readFoldersData(): MMKV = kv_folder_time
        suspend fun writeData(context: Context) {
            kv_video.clearAll()
            kv_folder_time.clearAll()
            val contentResolver = context.contentResolver
            measureNanoTime {
                contentResolver.query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, null
                )!!.apply {
                    moveToPosition(-1)
                    var videoCount = 0
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
                        val folderPath = path.substring(0, path!!.lastIndexOf("/") + 1)
                        val content =
                            "$path\u001A$uri\u001A$timeStamp\u001A$title\u001A$size\u001A$folder\u001A$duration"
                        kv_folder_time.encode(folderPath, timeStamp)
                        kv_video.encode(
                            folderPath, if (kv_video.containsKey(folderPath)) {
                                "${kv_video.decodeString(folderPath)}\u001A\u001A$content"
                            } else {
                                content
                            }
                        )
                        videoCount += 1
                    }
                    Statistics.writeInfo(
                        Statistics.FOLDERS_COUNT,
                        kv_folder_time.allKeys()!!.size.toString()
                    )
                    Statistics.writeInfo(Statistics.VIDEOS_COUNT, videoCount.toString())
                    Glide.get(context).clearDiskCache()
                    close()
                }
            }.also { time ->
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        "Scanning is completed within ${time / 1_000_000} ms.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}