package com.clearpole.videoyoux._compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateSizeAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.dp
import com.clearpole.videoyoux._compose.ui.theme.VideoYouXTheme

class DevelopActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VideoYouXTheme(hideBar = false) {
                // A surface container using the 'background' color from the theme
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

    @Composable
    fun DynamicScreen() {
        var boxState: BoxState by remember { mutableStateOf(BoxState.CloseState) }
        val animateSizeAsState by animateSizeAsState(
            targetValue = Size(boxState.width.value, boxState.height.value),
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessMediumLow
            ), label = ""
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .width(animateSizeAsState.width.dp)
                    .height(animateSizeAsState.height.dp)
                    .shadow(elevation = 3.dp, shape = RoundedCornerShape(50.dp))
                    .background(color = MaterialTheme.colorScheme.primary),
            )
            Button(
                modifier = Modifier.padding(top = 30.dp, bottom = 5.dp),
                onClick = { boxState = BoxState.OpenState }) {
                Text(text = "弹出")
            }
            Button(
                modifier = Modifier.padding(vertical = 5.dp),
                onClick = { boxState = BoxState.MusicState }) {
                Text(text = "State0")
            }
            Button(
                modifier = Modifier.padding(vertical = 5.dp),
                onClick = { boxState = BoxState.PayState }) {
                Text(text = "State1")
            }
            Button(
                modifier = Modifier.padding(vertical = 5.dp),
                onClick = { boxState = BoxState.ChargeState }) {
                Text(text = "State2")
            }
            Button(
                modifier = Modifier.padding(vertical = 5.dp),
                onClick = { boxState = BoxState.CloseState}) {
                Text(text = "闭合")
            }
        }
    }

}

private sealed class BoxState(val height: Dp, val width: Dp) {
    object OpenState : BoxState(30.dp, 100.dp)
    // 默认状态
    object CloseState : BoxState(30.dp, 30.dp)

    // 充电状态
    object ChargeState : BoxState(30.dp, 170.dp)

    // 支付状态
    object PayState : BoxState(100.dp, 100.dp)

    // 音乐状态
    object MusicState : BoxState(170.dp, 340.dp)
}

