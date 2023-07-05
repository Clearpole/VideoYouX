package com.clearpole.videoyoux._compose

import android.animation.ObjectAnimator
import android.content.res.Configuration
import android.content.res.Resources
import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.util.Log
import android.view.View
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.core.animation.doOnEnd
import com.clearpole.videoyoux.Values.isSystem
import com.clearpole.videoyoux._assembly.EmptyControlVideo
import com.clearpole.videoyoux._compose.theme.VideoYouXTheme
import com.clearpole.videoyoux._utils.VideoInfo
import com.clearpole.videoyoux.databinding.ActivityPlayerBinding
import com.clearpole.videoyoux.databinding.ActivityPlayerLandBinding
import com.drake.serialize.intent.bundle
import com.drake.tooltip.toast
import com.google.android.material.color.DynamicColors
import com.google.android.material.slider.Slider
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder
import com.shuyu.gsyvideoplayer.listener.VideoAllCallBack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import kotlin.math.roundToInt

class MainPlayerActivity : ComponentActivity() {
    private val TAG = "MPA"
    private val uri: String by bundle()
    private val paths: String by bundle()
    private lateinit var player: EmptyControlVideo
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
                GSYVideoManager.releaseAllVideos()
                finish()
            }
        }
    }

    @Composable
    fun VideoPlayer(mThis: MainPlayerActivity) {
        AndroidView(factory = {
            EmptyControlVideo(it).apply {
                GSYVideoOptionBuilder().setUrl(uri).setLooping(true)
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
                    playerBack.setOnClickListener {
                        GSYVideoManager.releaseAllVideos()
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
                                player.onVideoPause()
                            }

                            override fun onStopTrackingTouch(slider: Slider) {
                                if (value == 0F) {
                                    player.seekTo(1L)
                                    player.onVideoResume()
                                } else {
                                    player.seekTo(value.toLong())
                                    player.onVideoResume()
                                }
                            }
                        })
                        addOnChangeListener { slider, value, fromUser ->
                            if (!isSystem && !fromUser) {
                                isSystem = true
                                val anim =
                                    ObjectAnimator.ofFloat(
                                        slider,
                                        "value",
                                        if (value < 990f) 0f else value - 990f,
                                        value
                                    )
                                try {
                                    anim.start()
                                    anim.doOnEnd { isSystem = false }
                                } catch (_: Exception) {
                                }
                            }
                        }
                    }
                    player.setGSYVideoProgressListener { _, _, currentPosition, duration ->
                        playNow.text = timeParse(currentPosition)
                        playAll.text = timeParse(duration)
                        playSlider.value = currentPosition.toFloat()
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
        player.setVideoAllCallBack(object : VideoAllCallBack {
            override fun onStartPrepared(url: String?, vararg objects: Any?) {
                if (binding != null) {
                    binding.playLoading.visibility = View.VISIBLE
                }
            }

            override fun onPrepared(url: String?, vararg objects: Any?) {
                if (binding != null) {
                    binding.playLoading.visibility = View.GONE
                }
            }

            override fun onClickStartIcon(url: String?, vararg objects: Any?) {}
            override fun onClickStartError(url: String?, vararg objects: Any?) {}
            override fun onClickStop(url: String?, vararg objects: Any?) {}
            override fun onClickStopFullscreen(url: String?, vararg objects: Any?) {}
            override fun onClickResume(url: String?, vararg objects: Any?) {}
            override fun onClickResumeFullscreen(url: String?, vararg objects: Any?) {}
            override fun onClickSeekbar(url: String?, vararg objects: Any?) {}
            override fun onClickSeekbarFullscreen(url: String?, vararg objects: Any?) {}
            override fun onAutoComplete(url: String?, vararg objects: Any?) {}
            override fun onComplete(url: String?, vararg objects: Any?) {}
            override fun onEnterFullscreen(url: String?, vararg objects: Any?) {}
            override fun onQuitFullscreen(url: String?, vararg objects: Any?) {}
            override fun onQuitSmallWidget(url: String?, vararg objects: Any?) {}
            override fun onEnterSmallWidget(url: String?, vararg objects: Any?) {}
            override fun onTouchScreenSeekVolume(url: String?, vararg objects: Any?) {}
            override fun onTouchScreenSeekPosition(url: String?, vararg objects: Any?) {}
            override fun onTouchScreenSeekLight(url: String?, vararg objects: Any?) {}
            override fun onPlayError(url: String?, vararg objects: Any?) {
                toast("播放错误")
            }

            override fun onClickStartThumb(url: String?, vararg objects: Any?) {}
            override fun onClickBlank(url: String?, vararg objects: Any?) {}
            override fun onClickBlankFullscreen(url: String?, vararg objects: Any?) {}
        })
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
