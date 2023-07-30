@file:Suppress("DEPRECATION")

package com.clearpole.videoyoux._compose

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.res.Resources
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.DecelerateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.core.animation.doOnEnd
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.datasource.DefaultDataSourceFactory
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.PlayerView
import com.clearpole.videoyoux.R
import com.clearpole.videoyoux._compose.ui.theme.VideoYouXTheme
import com.clearpole.videoyoux._models.PlayerSliderV2ViewModel
import com.clearpole.videoyoux._player.Media3PlayerUtils
import com.clearpole.videoyoux._utils.VideoInfo
import com.clearpole.videoyoux.databinding.ActivityPlayerBinding
import com.drake.serialize.intent.bundle
import com.google.android.material.color.DynamicColors
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@UnstableApi
class MainPlayerActivity : ComponentActivity() {
    private var requireUpdateUI = true
    private var seeked = true
    private var uri: Uri? by bundle()
    private var paths: String? by bundle()
    lateinit var exoPlayer : ExoPlayer
    private lateinit var playerLifecycleScope: Job
    private lateinit var info: ArrayList<String>
    private lateinit var playerSliderV2ViewModel: PlayerSliderV2ViewModel
    private var exoExist = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.data != null) {
            uri = intent.data
            paths = intent.data!!.path
        }
       exoExist = Media3PlayerUtils.getIfExoExist()
        if (!exoExist) {
            exoPlayer = ExoPlayer.Builder(this)
                .setRenderersFactory(DefaultRenderersFactory(this).setEnableDecoderFallback(false))
                .setUseLazyPreparation(false)
                .build()
            Media3PlayerUtils.exoPlayer = exoPlayer
        }else{
            exoPlayer = Media3PlayerUtils.exoPlayer!!
        }
        DynamicColors.applyToActivityIfAvailable(this)
        playerLifecycleScope = lifecycleScope.launch {}
        val requiredParams = arrayListOf(MediaMetadataRetriever.METADATA_KEY_DURATION)
        info = VideoInfo.get(this, uri!!, requiredParams)
        playerSliderV2ViewModel = ViewModelProvider(this)[PlayerSliderV2ViewModel::class.java]
        setContent {
            VideoYouXTheme(hideBar = false, darkBar = true) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    VideoPlayer(exoExist)
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
    fun VideoPlayer(exoExist:Boolean) {
        AndroidView(modifier = Modifier
            .background(Color.Black), factory = {
            PlayerView(it).apply {
                if (!exoExist) {
                    exoPlayer.repeatMode = Player.REPEAT_MODE_ONE
                    val dataSourceFactory =
                        DefaultDataSourceFactory(context, Util.getUserAgent(context, "ExoPlayer"))
                    val mediaSource =
                        ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(
                            MediaItem.fromUri(uri!!)
                        )
                    exoPlayer.prepare(mediaSource)
                }
                useController = false
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
                AndroidViewBinding(factory = ActivityPlayerBinding::inflate) {
                    playMask.apply {
                        this.setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                        setContent {
                            // @formatter:off
                            Column(Modifier.fillMaxSize()) { Column(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Black.copy(alpha = 0.5f), Color.Transparent))).weight(1f, true)) {}
                            Column(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.5f)))).weight(1f, true)) {}}
                            // @formatter:on
                        }
                    }
                    playerBack.setOnClickListener {
                        end()
                    }
                    playPause.setOnClickListener {
                        playPause(this@AndroidViewBinding)
                    }
                    pausePlay.setOnClickListener {
                        playPause(this)
                    }
                    playPrimaryControlLayer.apply {
                        this.setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                        setContent {
                            Box(modifier = Modifier
                                .fillMaxSize()
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onTap = {
                                            playControlUiLayer.visibility.apply {
                                                AlphaAnimation(
                                                    if (this == View.VISIBLE) 1f else 0f,
                                                    if (this == View.VISIBLE) 0f else 1f
                                                ).apply {
                                                    duration = 110
                                                    playControlUiLayer.startAnimation(this)
                                                }
                                                playControlUiLayer.visibility =
                                                    if (this == View.VISIBLE) {
                                                        ImmersionBar
                                                            .with(this@MainPlayerActivity)
                                                            .hideBar(BarHide.FLAG_HIDE_BAR)
                                                            .init()
                                                        View.GONE
                                                    } else {
                                                        ImmersionBar
                                                            .with(this@MainPlayerActivity)
                                                            .hideBar(BarHide.FLAG_SHOW_BAR)
                                                            .init()
                                                        View.VISIBLE
                                                    }
                                            }
                                        },
                                        onDoubleTap = {
                                            playPause(this@AndroidViewBinding)
                                        },
                                        onLongPress = {

                                        }
                                    )
                                }) {
                            }
                        }
                    }
                    playerTitle.text = try {
                        paths!!.substring(paths!!.lastIndexOf("/") + 1, paths!!.lastIndexOf("."))
                    } catch (_: Exception) {
                        uri.toString()
                    }
                    playSliderV2.apply {
                        this.setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                        setContent {
                            val progress by remember {
                                playerSliderV2ViewModel.nowPosition
                            }
                            val maxPosition by remember {
                                playerSliderV2ViewModel.maxPosition
                            }
                            val animatedProgress by animateFloatAsState(
                                targetValue = progress, label = "",
                            )
                            Slider(
                                value = animatedProgress, onValueChange = {
                                    requireUpdateUI = false
                                    playerSliderV2ViewModel.valueChanging.value = true
                                    playerSliderV2ViewModel.nowPosition.value = it
                                    updateUI(binding = this@AndroidViewBinding)
                                }, valueRange = 0f..maxPosition,
                                onValueChangeFinished = {
                                    seeked = true
                                    translationAlphaAnim(playLoading, true)
                                    playerSliderV2ViewModel.valueChanging.value = false
                                    exoPlayer.seekTo(playerSliderV2ViewModel.nowPosition.value.toLong())
                                    requireUpdateUI = true
                                },
                                modifier = Modifier.padding(horizontal = 10.dp)
                            )
                        }
                    }
                    playerListenerLogic(this, exoExist)
                }
            }
    }

    private fun playPause(binding: ActivityPlayerBinding) {
        if (exoPlayer.isPlaying) {
            exoPlayer.pause()
            playPauseView(binding, true)
        } else {
            exoPlayer.play()
            playPauseView(binding, false)
        }
    }

    private fun playPauseView(binding: ActivityPlayerBinding, isPlaying: Boolean) {
        val playIcon =
            AppCompatResources.getDrawable(this@MainPlayerActivity, R.drawable.round_play_arrow_24)
        val pauseIcon =
            AppCompatResources.getDrawable(this@MainPlayerActivity, R.drawable.round_pause_24)
        AnimatorSet().apply {
            interpolator = DecelerateInterpolator(0.9f)
            playTogether(
                if (isPlaying) playTogetherAnim(binding.playPause, 100f, 0f, 0f, 1f, 150L)
                else playTogetherAnim(binding.playPause, 0f, -100f, 1f, 0f, 150L)
            )
            if (isPlaying) {
                binding.playPause.visibility = View.VISIBLE
                binding.pausePlay.icon = playIcon
            }
            start()
            doOnEnd {
                if (!isPlaying) {
                    binding.pausePlay.icon = pauseIcon
                    binding.playPause.visibility = View.GONE
                }
                cancel()
            }
        }
    }

    private fun translationAlphaAnim(view: View, visibility: Boolean) {
        AnimatorSet().apply {
            interpolator = DecelerateInterpolator(0.9f)
            playTogether(
                if (visibility) playTogetherAnim(view, 100f, 0f, 0f, 1f, 200L)
                else playTogetherAnim(view, 0f, -100f, 1f, 0f, 200L)
            )
            if (visibility) {
                view.visibility = View.VISIBLE
            }
            start()
            doOnEnd {
                if (!visibility) {
                    view.visibility = View.GONE
                }
                cancel()
            }
        }
    }

    private fun playTogetherAnim(
        view: View,
        t: Float,
        tE: Float,
        a: Float,
        aE: Float,
        tS: Long
    ): MutableList<Animator> {
        return mutableListOf(
            ObjectAnimator
                .ofFloat(
                    view,
                    "translationY", t, tE
                )
                .apply { duration = tS },
            ObjectAnimator
                .ofFloat(
                    view,
                    "alpha", a, aE
                )
                .apply { duration = tS }
        )
    }

    private fun playerListenerLogic(
        binding: ActivityPlayerBinding? = null,
        exoExist:Boolean
    ) {
        playerLifecycleScope = lifecycleScope.launch {
            while (true) {
                if (requireUpdateUI) {
                    updateUI(binding = binding!!)
                    delay(500)
                }
                delay(500)
            }
        }
            exoPlayer.addListener(object : Player.Listener {
                @SuppressLint("SwitchIntDef")
                override fun onPlaybackStateChanged(playbackState: Int) {
                    super.onPlaybackStateChanged(playbackState)
                    when (playbackState) {
                        Player.STATE_READY -> {
                            exoPlayer.play()
                        }
                    }
                }

                override fun onIsLoadingChanged(isLoading: Boolean) {
                    super.onIsLoadingChanged(isLoading)
                    if (!isLoading) {
                        if (seeked) {
                            translationAlphaAnim(binding!!.playLoading, false)
                            seeked = false
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

        if (exoExist){
            //存在
            //重载不会重新调用事件，手动隐藏
            binding!!.playLoading.visibility = View.GONE
        }
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
        Media3PlayerUtils.exoPlayer = null
        finish()
    }

    private fun timeParse(duration: Long): String {
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
