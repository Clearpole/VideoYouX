package com.clearpole.videoyoux._compose

import android.content.res.Configuration
import android.content.res.Resources
import android.media.MediaMetadataRetriever
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
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.clearpole.videoyoux._compose.theme.VideoYouXTheme
import com.clearpole.videoyoux._utils.VideoInfo
import com.clearpole.videoyoux.databinding.ActivityPlayerBinding
import com.clearpole.videoyoux.databinding.ActivityPlayerLandBinding
import com.drake.serialize.intent.bundle
import com.google.android.material.color.DynamicColors
import com.google.android.material.slider.Slider
import kotlin.math.roundToInt

class MainPlayerActivity : ComponentActivity() {
    private val TAG = "MPA"
    private val uri: String by bundle()
    private val paths: String by bundle()
    private lateinit var exoPlayer: ExoPlayer
    private lateinit var info: ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DynamicColors.applyToActivityIfAvailable(this)
        val requiredParams = arrayListOf(MediaMetadataRetriever.METADATA_KEY_DURATION)
        info = VideoInfo.get(paths, requiredParams)
        setContent {
            VideoYouXTheme(hideBar = false, darkBar = true) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    VideoPlayer(mThis = this)
                    ControlLayout(mThis = this, resources = resources)
                }
            }
            BackHandler {
                finish()
            }
        }
    }

    @Composable
    fun VideoPlayer(mThis: MainPlayerActivity) {
        AndroidView(factory = {
            androidx.media3.ui.PlayerView(it).apply {
                useController = false
                exoPlayer = ExoPlayer.Builder(mThis).build()
                exoPlayer.addMediaItem(MediaItem.fromUri(uri))
                player = exoPlayer
                exoPlayer.prepare()
                exoPlayer.playWhenReady = true
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
                    playerBack.setOnClickListener {
                        finish()
                    }
                    playerTitle.text =
                        paths.substring(paths.lastIndexOf("/") + 1, paths.lastIndexOf("."))
                    playSlider.apply {
                        valueTo = info[0].toFloat()
                        setLabelFormatter { value: Float ->
                            return@setLabelFormatter timeParse(value.toLong()).toString()
                        }
                        addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
                            override fun onStartTrackingTouch(slider: Slider) {
                            }

                            override fun onStopTrackingTouch(slider: Slider) {
                                if (value == 0F) {

                                } else {

                                }
                            }
                        })
                    }

                    playerListenerLogic(this)
                }
            }
        }
    }

    private fun playerListenerLogic(
        binding: ActivityPlayerBinding? = null,
        bindingLand: ActivityPlayerLandBinding? = null
    ) {

    }

    private fun timeParse(duration: Long): String {
        var time: String? = ""
        val minute = duration / 60000
        val seconds = duration % 60000
        val second = (seconds.toFloat() / 1000).roundToInt().toLong()
        if (minute < 10) {
            time += "0"
        }
        time += "$minute:"
        if (second < 10) {
            time += "0"
        }
        time += second
        return time!!.trim()
    }
}
