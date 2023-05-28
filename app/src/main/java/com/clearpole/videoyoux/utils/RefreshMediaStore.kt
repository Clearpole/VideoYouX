package com.clearpole.videoyoux.utils

import android.content.Context
import android.content.Intent
import android.media.MediaScannerConnection

class RefreshMediaStore {
    companion object {
        @Suppress("DEPRECATION")
        fun updateMedia(context: Context, path: String) {
            MediaScannerConnection.scanFile(
                context, arrayOf(path), null
            ) { _, uri ->
                val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                mediaScanIntent.data = uri
                context.sendBroadcast(mediaScanIntent)
            }
        }
    }
}
