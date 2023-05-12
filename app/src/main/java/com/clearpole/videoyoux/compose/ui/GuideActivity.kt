@file:OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)

package com.clearpole.videoyoux.compose.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.blankj.utilcode.util.TimeUtils
import com.clearpole.videoyoux.compose.ui.theme.VideoYouXTheme
import kotlinx.coroutines.delay

class GuideActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VideoYouXTheme(hideBar = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val step = remember {
                        mutableStateOf(1)
                    }
                    val message = remember {
                        mutableStateOf("你好，Vyx！")
                    }
                    val guideText = remember {
                        mutableStateOf("现在是...\nVyx-TIME！")
                    }
                    val guideTextMore = remember {
                        mutableStateOf("欢迎来到Vyx大陆ヾ(≧ ▽ ≦)ゝ")
                    }
                    Column(modifier = Modifier.fillMaxSize()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 60.dp)
                        ) {

                            Column(modifier = Modifier.fillMaxSize()) {
                                Typewriter(
                                    fixedText = "",
                                    animatedText = guideText.value,
                                    Orientation.Vertical
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                Typewriter(
                                    fixedText = "",
                                    animatedText = guideTextMore.value,
                                    Orientation.Horizontal,25,1
                                )
                            }

                            Row(
                                modifier = Modifier.align(
                                    Alignment.BottomCenter
                                )
                            ) {
                                OutlinedTextField(
                                    value = message.value, onValueChange = { message.value = it },
                                    shape = MaterialTheme.shapes.large,
                                    modifier = Modifier.height(60.dp),
                                    readOnly = true
                                )
                                Spacer(modifier = Modifier.width(15.dp))
                                AnimatedContent(targetState =TimeUtils.getNowMills(), label = "") {
                                    IconButton(
                                        onClick = {
                                            when (step.value) {
                                                1 -> {
                                                    guideText.value = "让我们先\n进行一些设置吧！"
                                                    message.value = "我准备好了！"
                                                    step.value = 2
                                                }
                                                2-> {
                                                    guideText.value = "首先，\n我需要一些权限..."
                                                    message.value = "我自愿授予你这些权限"
                                                    guideTextMore.value = "1. 存储权限：用于存储数据\n2. 访问权限：用于访问媒体库"
                                                }
                                            }
                                        },
                                        modifier = Modifier
                                            .size(60.dp)
                                            .clip(RoundedCornerShape(30.dp)),
                                        colors = IconButtonDefaults.filledIconButtonColors()
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Send,
                                            contentDescription = "send",
                                            tint = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                BackHandler {
                    finish()
                }
            }
        }
    }

    @Composable
    fun Typewriter(
        fixedText: String,
        animatedText: String,
        orientation: Orientation = Orientation.Horizontal,
        delay: Long = 50,
        type: Int = 0
    ) {
        var mAnimatedText by remember { mutableStateOf("") }
        val textToDisplay = "$fixedText$mAnimatedText"

        LaunchedEffect(key1 = animatedText) {
            animatedText.forEachIndexed { charIndex, _ ->
                mAnimatedText = animatedText.substring(startIndex = 0, endIndex = charIndex + 1)
                delay(delay)
                if (charIndex + 1 < animatedText.length) {
                    mAnimatedText += when (orientation) {
                        Orientation.Vertical -> "|"
                        Orientation.Horizontal -> "_"
                        else -> {}
                    }
                    delay(30)
                }

            }
        }
        when (type) {
            0 -> {
                Text(
                    text = textToDisplay,
                    style = TextStyle(
                        fontSize = 40.sp,
                        letterSpacing = -(1.6).sp,
                        lineHeight = 52.sp,
                    ),
                    modifier = Modifier.padding(top = 80.dp, start = 30.dp,end = 25.dp)
                )
            }

            else -> {
                Text(
                    text = textToDisplay,
                    style = TextStyle(
                        fontSize = 16.sp,
                        letterSpacing = -(1.6).sp,
                        lineHeight = 26.sp,
                    ),
                    modifier = Modifier.padding(top = 10.dp, start = 32.dp, end = 25.dp)
                )
            }
        }
    }
}

