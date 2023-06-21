package com.clearpole.videoyoux._compose

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.viewinterop.AndroidViewBinding
import com.clearpole.videoyoux._assembly.EmptyControlVideo
import com.clearpole.videoyoux._compose.theme.VideoYouXTheme
import com.clearpole.videoyoux.databinding.ActivityPlayerBinding
import com.drake.serialize.intent.bundle
import com.drake.tooltip.toast
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder

class MainPlayerActivity : ComponentActivity() {
    private val uri: String by bundle()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VideoYouXTheme(hideBar = true) {
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
                GSYVideoOptionBuilder().setCacheWithPlay(true).setUrl(uri).setLooping(true).build(this)
                this.startPlayLogic()
            }
        })
    }
}

@Composable
fun ControlLayout(mThis: MainPlayerActivity,resources: Resources) {
    Box(Modifier.fillMaxSize()) {
        // @formatter:off
        Column(Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Black.copy(alpha = 0.5f), Color.Transparent))).weight(1f,true)) {}
            Column(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent,Color.Black.copy(alpha = 0.5f)))).weight(1f,true)) {}
        }
        // @formatter:on
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){

        }else {
            AndroidViewBinding(factory = ActivityPlayerBinding::inflate) {

            }
        }
    }
}