package com.clearpole.videoyoux._compose

import android.content.res.Configuration
import android.content.res.Resources
import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.viewinterop.AndroidViewBinding
import com.clearpole.videoyoux._assembly.EmptyControlVideo
import com.clearpole.videoyoux._compose.theme.VideoYouXTheme
import com.clearpole.videoyoux._utils.VideoInfo
import com.clearpole.videoyoux.databinding.ActivityPlayerBinding
import com.clearpole.videoyoux.databinding.ActivityPlayerLandBinding
import com.drake.serialize.intent.bundle
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder

class MainPlayerActivity : ComponentActivity() {
    private val uri: String by bundle()
    private val paths: String by bundle()
    private lateinit var player: EmptyControlVideo
    private lateinit var info: ArrayList<String>
    private val TAG = "MPA"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val requiredParams = arrayListOf(MediaMetadataRetriever.METADATA_KEY_DURATION)
        info = VideoInfo.get(paths,requiredParams)
        Log.e(TAG, "onCreate:$info", )
        setContent {
            var code by remember {
                mutableStateOf(0)
            }
            VideoYouXTheme(hideBar = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    VideoPlayer(mThis = this)
                    ControlLayout(mThis = this, resources = resources)
                }
            }
            BackHandler {
                GSYVideoManager.releaseAllVideos()
                finish()
            }
        }
    }

    @Composable
    fun VideoPlayer(mThis: MainPlayerActivity) {
        AndroidView(factory = {
            EmptyControlVideo(it).apply {
                GSYVideoOptionBuilder().setCacheWithPlay(true).setUrl(uri).setLooping(true)
                    .build(this)
                player = this
                player.startPlayLogic()
            }
        })
    }

    @Composable
    fun ControlLayout(mThis: MainPlayerActivity, resources: Resources) {
        Box(
            Modifier
                .fillMaxSize()
                .layoutId(LocalConfiguration.current)
        ) {
            // @formatter:off
            Column(Modifier.fillMaxSize()) {
                Column(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Black.copy(alpha = 0.5f), Color.Transparent))).weight(1f,true)) {}
                Column(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent,Color.Black.copy(alpha = 0.5f)))).weight(1f,true)) {}
            }
            // @formatter:on
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                AndroidViewBinding(factory = ActivityPlayerLandBinding::inflate) {

                }
            } else {
                AndroidViewBinding(factory = ActivityPlayerBinding::inflate) {
                    this.playerTitle.text = paths.substring(paths.lastIndexOf("/") + 1, paths.lastIndexOf("."))
                }
            }
        }
    }
}
