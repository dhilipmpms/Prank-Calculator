package com.darkempire78.opencalculator.prank.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.darkempire78.opencalculator.R
import com.darkempire78.opencalculator.prank.utils.PrankPreferences
import java.io.File

class PrankSettingsFragment : PreferenceFragmentCompat() {

    private val audioPickerLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let { handleSelectedAudioUri(it) }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.prank_preferences, rootKey)

        val audioFilePref = findPreference<Preference>("prank_audio_file")
        audioFilePref?.setOnPreferenceClickListener {
            audioPickerLauncher.launch(arrayOf("audio/*"))
            true
        }

        val settings = PrankPreferences(requireContext()).getPrankSettings()
        settings.audioUri?.let {
            updateAudioFileSummary(it)
        }
    }

    private fun handleSelectedAudioUri(uri: Uri) {
        val context = requireContext()
        try {
            context.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        } catch (e: Exception) {
            Log.w("PRANK", "Could not take persistable permission: ${e.message}")
        }

        val savedUri = copyAudioToInternalStorage(context, uri) ?: uri
        PrankPreferences(context).setAudioUri(savedUri)
        updateAudioFileSummary(savedUri)
    }

    private fun copyAudioToInternalStorage(context: Context, uri: Uri): Uri? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val prankDir = File(context.filesDir, "prank_audio").apply { mkdirs() }
            val localFile = File(prankDir, "selected_prank_audio.mp3")
            localFile.outputStream().use { output ->
                inputStream.copyTo(output)
            }
            Uri.fromFile(localFile)
        } catch (e: Exception) {
            Log.e("PRANK", "Error copying audio to internal storage: ${e.message}", e)
            null
        }
    }

    private fun updateAudioFileSummary(uri: Uri) {
        val audioFilePref = findPreference<Preference>("prank_audio_file")
        audioFilePref?.summary = uri.lastPathSegment ?: uri.toString()
    }
}
