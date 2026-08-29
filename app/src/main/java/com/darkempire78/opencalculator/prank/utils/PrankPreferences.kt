package com.darkempire78.opencalculator.prank.utils

import android.content.Context
import android.net.Uri
import androidx.preference.PreferenceManager
import com.darkempire78.opencalculator.prank.model.PrankSettings
import com.darkempire78.opencalculator.prank.model.TriggerMode

class PrankPreferences(private val context: Context) {
    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun getPrankSettings(): PrankSettings {
        val isEnabled = sharedPreferences.getBoolean("prank_enabled", false)
        val modeString = sharedPreferences.getString("prank_trigger_mode", TriggerMode.EQUALS.name) ?: TriggerMode.EQUALS.name
        val triggerMode = try {
            TriggerMode.valueOf(modeString)
        } catch (e: Exception) {
            TriggerMode.EQUALS
        }
        val triggerValueStr = sharedPreferences.getString("prank_trigger_value", "10") ?: "10"
        val triggerValue = triggerValueStr.toDoubleOrNull() ?: 10.0
        val audioUriStr = sharedPreferences.getString("prank_audio_uri", null)
        val audioUri = if (!audioUriStr.isNullOrEmpty()) Uri.parse(audioUriStr) else null
        val isAudioLooping = sharedPreferences.getBoolean("prank_audio_looping", false)

        return PrankSettings(
            isPrankEnabled = isEnabled,
            triggerMode = triggerMode,
            triggerValue = triggerValue,
            audioUri = audioUri,
            isAudioLooping = isAudioLooping
        )
    }

    fun setAudioUri(uri: Uri?) {
        sharedPreferences.edit().putString("prank_audio_uri", uri?.toString()).apply()
    }
}
