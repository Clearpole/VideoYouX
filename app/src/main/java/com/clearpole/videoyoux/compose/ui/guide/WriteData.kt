package com.clearpole.videoyoux.compose.ui.guide

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class WriteData {
    companion object{
        @Composable
        fun Start(){
            Column(Modifier.fillMaxSize()) {
                Text(
                    text = "刷新媒体库\n\n写入本地数据库",
                    fontSize = 30.sp,
                    modifier = Modifier.padding(top = 80.dp, start = 30.dp)
                )
            }
        }
    }
}