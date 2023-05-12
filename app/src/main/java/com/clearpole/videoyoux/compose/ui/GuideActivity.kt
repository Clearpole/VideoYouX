@file:OptIn(ExperimentalAnimationApi::class)

package com.clearpole.videoyoux.compose.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.clearpole.videoyoux.NavHostIn
import com.clearpole.videoyoux.compose.ui.GuideActivity.GuideText.STEP_PERMISSION
import com.clearpole.videoyoux.compose.ui.GuideActivity.GuideText.STEP_WELCOME
import com.clearpole.videoyoux.compose.ui.guide.Permission
import com.clearpole.videoyoux.compose.ui.guide.Welcome
import com.clearpole.videoyoux.compose.ui.theme.VideoYouXTheme
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

class GuideActivity : ComponentActivity() {
    object GuideText {
        const val STEP_WELCOME = "你好！Vyx！"
        const val STEP_PERMISSION = "申请权限"
        const val STEP_WRITE_DATA = "刷新数据库"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VideoYouXTheme(hideBar = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberAnimatedNavController()
                    val step = remember {
                        mutableStateOf(STEP_WELCOME)
                    }
                    Box(modifier = Modifier.fillMaxSize()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomStart)
                                .padding(bottom = 60.dp)
                        ) {
                            NavHost(navController)
                            Row(
                                modifier = Modifier.align(
                                    Alignment.BottomEnd
                                )
                            ) {
                                Button(modifier = Modifier.animateContentSize(finishedListener = { _, _ -> }),
                                    onClick = {
                                        when (step.value) {
                                            STEP_WELCOME -> {
                                                navController.navigate(NavHostIn.NAV_GUIDE_PERMISSION)
                                                step.value = STEP_PERMISSION
                                            }

                                            STEP_PERMISSION -> {

                                            }

                                            else -> {
                                            }
                                        }
                                    },
                                    contentPadding = PaddingValues(
                                        top = 10.dp, bottom = 10.dp, start = 35.dp, end = 35.dp
                                    ),
                                    content = {
                                        AnimatedContent(targetState = step.value,
                                            label = "",
                                            transitionSpec = {
                                                slideInHorizontally { width -> width } + fadeIn() with slideOutHorizontally { width -> -width } + fadeOut()
                                            }) {
                                            Text(text = step.value, fontSize = 17.sp)
                                        }
                                    })
                                Spacer(modifier = Modifier.width(35.dp))
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
    fun NavHost(navController: NavHostController) {
        AnimatedNavHost(
            navController = navController,
            startDestination = NavHostIn.NAV_GUIDE_WELCOME
        ) {
            composable(NavHostIn.NAV_GUIDE_WELCOME, enterTransition = {
                slideInHorizontally { width -> width }
            }, exitTransition = {
                slideOutHorizontally { width -> -width }
            }) {
                Welcome.Start()
            }
            composable(
                NavHostIn.NAV_GUIDE_PERMISSION,
                enterTransition = {
                    slideInHorizontally { width -> width }
                },
                exitTransition = {
                    slideOutHorizontally { width -> -width }
                }) {
                Permission.Start()
            }
            composable(
                NavHostIn.NAV_GUIDE_WRITE_DATA,
                enterTransition = {
                    slideInHorizontally { width -> width }
                },
                exitTransition = {
                    slideOutHorizontally { width -> -width }
                }) {
            }
        }
    }
}

