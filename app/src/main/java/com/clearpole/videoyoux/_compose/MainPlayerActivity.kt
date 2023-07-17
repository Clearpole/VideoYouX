package com.clearpole.videoyoux._compose

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.content.res.Resources
import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.clearpole.videoyoux._compose.theme.VideoYouXTheme
import com.clearpole.videoyoux._models.PlayerSliderV2ViewModel
import com.clearpole.videoyoux._utils.VideoInfo
import com.clearpole.videoyoux.databinding.ActivityPlayerBinding
import com.clearpole.videoyoux.databinding.ActivityPlayerLandBinding
import com.drake.serialize.intent.bundle
import com.google.android.material.color.DynamicColors
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@UnstableApi
class MainPlayerActivity : ComponentActivity() {
    private val TAG = "MPA"
    private var requireUpdateUI = true
    private var once = true
    private val uri: String by bundle()
    private val paths: String by bundle()
    private lateinit var playerLifecycleScope: Job
    private lateinit var exoPlayer: ExoPlayer
    private lateinit var info: ArrayList<String>

    private lateinit var playerSliderV2ViewModel: PlayerSliderV2ViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exoPlayer = ExoPlayer.Builder(this)
            .setRenderersFactory(DefaultRenderersFactory(this).setEnableDecoderFallback(true))
            .build()
        DynamicColors.applyToActivityIfAvailable(this)
        val requiredParams = arrayListOf(MediaMetadataRetriever.METADATA_KEY_DURATION)
        info = VideoInfo.get(paths, requiredParams)
        playerSliderV2ViewModel = ViewModelProvider(this)[PlayerSliderV2ViewModel::class.java]
        setContent {
            VideoYouXTheme(hideBar = false, darkBar = true) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    VideoPlayer()
                    ControlLayout(resources = resources)
                }
            }
            BackHandler {
                end()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        exoPlayer.pause()
    }

    override fun onResume() {
        super.onResume()
        exoPlayer.play()
    }


    @Composable
    fun VideoPlayer() {
        AndroidView(modifier = Modifier.background(Color.Black), factory = {
            PlayerView(it).apply {
                useController = false
                exoPlayer.repeatMode = Player.REPEAT_MODE_ONE
                exoPlayer.addMediaItem(MediaItem.fromUri(uri))
                exoPlayer.prepare()
                exoPlayer.playWhenReady = true
                player = exoPlayer
            }
        })
    }

    @Composable
    fun ControlLayout(resources: Resources) {
        Box(
            Modifier
                .fillMaxSize()
                .layoutId(LocalConfiguration.current)
        ) {
            // @formatter:off
            Column(Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.Black.copy(alpha = 0.5f),
                                    Color.Transparent
                                )
                            )
                        )
                        .weight(1f, true)
                ) {}
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.5f)
                                )
                            )
                        )
                        .weight(1f, true)
                ) {}
            }
            // @formatter:on
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                AndroidViewBinding(factory = ActivityPlayerLandBinding::inflate) {

                }
            } else {
                AndroidViewBinding(factory = ActivityPlayerBinding::inflate) {
                    playerBack.setOnClickListener {
                        end()
                    }
                    playerTitle.text =
                        paths.substring(paths.lastIndexOf("/") + 1, paths.lastIndexOf("."))
                    playSliderV2.apply {
                        /*
                        setLabelFormatter { value: Float ->
                            return@setLabelFormatter timeParse(value.toLong())
                        }
                        addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
                            override fun onStartTrackingTouch(slider: Slider) {
                                slider.value = slider.value
                                requireUpdateUI = false
                                exoPlayer.pause()
                            }

                            override fun onStopTrackingTouch(slider: Slider) {
                                exoPlayer.seekTo(slider.value.toLong())
                                exoPlayer.play()
                                requireUpdateUI = true
                            }
                        })
                        */
                        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                        setContent {
                            var progress by remember {
                                playerSliderV2ViewModel.nowPosition
                            }
                            var maxPosition by remember {
                                playerSliderV2ViewModel.maxPosition
                            }
                            val animatedProgress by animateFloatAsState(
                                targetValue = progress,
                            )
                            Slider(
                                value = animatedProgress, onValueChange = {
                                    requireUpdateUI = false
                                    playerSliderV2ViewModel.valueChanging.value = true
                                    playerSliderV2ViewModel.nowPosition.value = it
                                    updateUI(binding = this@AndroidViewBinding)
                                }, valueRange = 0f..maxPosition,
                                onValueChangeFinished = {
                                    playerSliderV2ViewModel.valueChanging.value = false
                                    exoPlayer.seekTo(playerSliderV2ViewModel.nowPosition.value.toLong())
                                    //exoPlayer.play()
                                    requireUpdateUI = true
                                },
                                modifier = Modifier.padding(horizontal = 10.dp)
                            )
                        }
                    }
                    playerListenerLogic(this)
                }
            }
        }
    }

    private fun playerListenerLogic(
        binding: ActivityPlayerBinding? = null
    ) {
        exoPlayer.addListener(object : Player.Listener {
            @SuppressLint("SwitchIntDef")
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                when (playbackState) {
                    Player.STATE_READY -> {
                        if (once) {
                            once = false
                            binding!!.playLoading.visibility = View.GONE
                            playerLifecycleScope = lifecycleScope.launch {
                                while (true) {
                                    if (requireUpdateUI) {
                                        updateUI(binding = binding)
                                        delay(500)
                                    }
                                    delay(500)
                                }
                            }
                        }
                    }
                }
            }

            override fun onPlayerError(error: PlaybackException) {
                super.onPlayerError(error)
                playerLifecycleScope.cancel()
                MaterialAlertDialogBuilder(this@MainPlayerActivity).setTitle("播放错误")
                    .setMessage("name:\n${error.errorCodeName}\n\ncode:${error.errorCode}\n\nmessage:\n${error.message}")
                    .setPositiveButton("退出") { _, _ ->
                        end()
                    }.show()
            }
        })
    }

    private fun updateUI(binding: ActivityPlayerBinding) {
        binding.apply {
            val currentPosition = exoPlayer.currentPosition
            val duration = exoPlayer.duration
            playNow.text = if (playerSliderV2ViewModel.valueChanging.value) {
                timeParse(playerSliderV2ViewModel.nowPosition.value.toLong())
            } else {
                playerSliderV2ViewModel.nowPosition.value = currentPosition.toFloat()
                timeParse(currentPosition)
            }
            playAll.text = timeParse(duration)
            /*
            playSlider.valueTo = duration.toFloat()
            val to = currentPosition + 1000f
            val anim =
                ObjectAnimator.ofFloat(
                    binding.playSlider,
                    "value",
                    currentPosition.toFloat(),
                    if (to <= duration) to else duration.toFloat()
                )
            anim.start()
            anim.doOnEnd {
                anim.cancel()
            }
            */
            val thisMaxPosition = exoPlayer.duration.toFloat()
            if (thisMaxPosition >= 0) {
                playerSliderV2ViewModel.maxPosition.value = thisMaxPosition
            }
        }
    }

    private fun end() {
        playerLifecycleScope.cancel()
        exoPlayer.stop()
        exoPlayer.release()
        finish()
    }

    private fun timeParse(duration: Long): String {
        /*
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
        */
        val hours = duration / 3600000
        val minutes = (duration % 3600000) / 60000
        val seconds = (duration % 60000) / 1000
        return if (hours > 0) {
            String.format("%02d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format("%02d:%02d", minutes, seconds)
        }
    }
}
