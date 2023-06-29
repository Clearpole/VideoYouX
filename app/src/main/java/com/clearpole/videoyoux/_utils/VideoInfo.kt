package com.clearpole.videoyoux._utils

import android.media.MediaMetadataRetriever
import android.util.Log

class VideoInfo {
    companion object {
        fun get(paths: String, list: ArrayList<Int>): ArrayList<String> {
            //list: MediaMetadataRetriever.METADATA_KEY_xxx
            val resultList = arrayListOf<String>()
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(paths)
            for (type in list) {
                resultList.add(
                    retriever.extractMetadata(type)
                        .toString()
                )
                Log.e("MPA",type.toString())
            }
            retriever.release()
            return resultList
        }
    }
}