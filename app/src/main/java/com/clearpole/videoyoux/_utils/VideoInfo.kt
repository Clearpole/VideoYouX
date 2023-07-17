package com.clearpole.videoyoux._utils

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri

class VideoInfo {
    companion object {
        fun get(context: Context, uri: Uri, list: ArrayList<Int>): ArrayList<String> {
            //list: MediaMetadataRetriever.METADATA_KEY_xxx
            val resultList = arrayListOf<String>()
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(context, uri)
            for (type in list) {
                resultList.add(
                    retriever.extractMetadata(type)
                        .toString()
                )
            }
            retriever.release()
            return resultList
        }
    }
}