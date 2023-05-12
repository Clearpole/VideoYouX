package com.clearpole.videoyoux.compose.ui.guide

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class Permission {
    companion object{
        @Composable
        fun Start(){
            Column(Modifier.fillMaxSize()) {
                Text(
                    text = "在开始之前\n\n您需要放行一些权限",
                    fontSize = 30.sp,
                    modifier = Modifier.padding(top = 80.dp, start = 30.dp)
                )
            }
        }
    }
}