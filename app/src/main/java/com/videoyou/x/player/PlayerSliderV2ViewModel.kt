package com.videoyou.x.player

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class PlayerSliderV2ViewModel : ViewModel() {
    val nowPosition = mutableStateOf(0f)
    val maxPosition = mutableStateOf(0f)
    val valueChanging = mutableStateOf(false)
}