package com.clearpole.videoyoux._compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateSizeAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.clearpole.videoyoux._compose.ui.theme.VideoYouXTheme
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DevelopActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VideoYouXTheme(hideBar = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        DynamicScreen()
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    fun DynamicScreen() {
        var boxState: BoxState by remember { mutableStateOf(BoxState.CloseState) }
        var isIconShow: Boolean by remember { mutableStateOf(false) }
        var isTextShow: Boolean by remember { mutableStateOf(false) }
        var isIconOpened: Boolean by remember {
            mutableStateOf(false)
        }
        val animateSizeAsState by animateSizeAsState(
            targetValue = Size(boxState.width.value, boxState.height.value),
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessMediumLow
            ), label = ""
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Column(
                modifier = Modifier
                    .width(animateSizeAsState.width.dp)
                    .height(animateSizeAsState.height.dp)
                    .shadow(elevation = 3.dp, shape = RoundedCornerShape(50.dp))
                    .background(color = MaterialTheme.colorScheme.primary),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedVisibility(visible = isIconShow, enter = fadeIn(), exit = fadeOut()) {
                    AnimatedContent(targetState = isTextShow, label = "") {
                        Row(verticalAlignment = Alignment.CenterVertically){
                            Icon(
                                Icons.Rounded.PlayArrow,
                                contentDescription = null,
                                modifier = Modifier.size(35.dp),
                                tint = Color.White,
                            )
                            AnimatedVisibility(visible = isIconOpened) {
                                Text(
                                    text = "已暂停", color = Color.White, fontSize = TextUnit(
                                        17f,
                                        TextUnitType.Sp
                                    ), maxLines = 1, modifier = Modifier.padding(horizontal = 10.dp)
                                )
                            }
                        }
                    }
                }
            }

            Button(
                modifier = Modifier.padding(vertical = 25.dp),
                onClick = {
                    if (!isIconOpened && !isIconShow && !isTextShow) {
                        lifecycleScope.launch {
                            isIconShow = true
                            boxState = BoxState.PayState
                            isTextShow = true
                            delay(600)
                            isIconOpened = true
                            if (isTextShow) {
                                boxState = BoxState.MusicState
                            }
                            cancel()
                        }
                    } else {
                        isIconShow = false
                        isTextShow = false
                        isIconOpened = false
                        boxState = BoxState.CloseState
                    }
                }) {
                Text(text = "GroupAnimation1")
            }
        }
    }

}

private sealed class BoxState(val height: Dp, val width: Dp) {
    data object CloseState : BoxState(30.dp, 30.dp)
    // 支付状态
    data object PayState : BoxState(60.dp, 60.dp)

    // 音乐状态
    data object MusicState : BoxState(50.dp, 160.dp)
}

