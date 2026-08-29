package com.darkempire78.opencalculator.prank.model

import android.net.Uri

enum class TriggerMode {
    EQUALS,
    DIVISIBLE_BY,
    CONTAINS_DIGIT
}

data class PrankSettings(
    val isPrankEnabled: Boolean = false,
    val triggerMode: TriggerMode = TriggerMode.EQUALS,
    val triggerValue: Double = 10.0,
    val audioUri: Uri? = null,
    val isAudioLooping: Boolean = false
)
