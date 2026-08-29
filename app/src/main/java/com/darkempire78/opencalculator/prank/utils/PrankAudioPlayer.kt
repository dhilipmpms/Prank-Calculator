package com.darkempire78.opencalculator.prank.utils

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log

class PrankAudioPlayer(context: Context) {
    private val appContext = context.applicationContext
    private var mediaPlayer: MediaPlayer? = null

    @Synchronized
    fun playPrankAudio(audioUri: Uri, isLooping: Boolean) {
        stopAudio()
        Log.d("PRANK", "PRANK: configured audio URI = $audioUri")
        Log.d("PRANK", "PRANK: initializing audio player")

        try {
            val player = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                setDataSource(appContext, audioUri)
                this.isLooping = isLooping

                setOnPreparedListener { mp ->
                    Log.d("PRANK", "PRANK: audio player prepared")
                    try {
                        Log.d("PRANK", "PRANK: starting playback")
                        mp.start()
                        Log.d("PRANK", "PRANK: playback started")
                    } catch (e: Exception) {
                        Log.e("PRANK", "PRANK: playback error = ${e.message}", e)
                    }
                }

                setOnErrorListener { _, what, extra ->
                    Log.e("PRANK", "PRANK: playback error = MediaPlayer error (what=$what, extra=$extra)")
                    true
                }

                setOnCompletionListener {
                    Log.d("PRANK", "PRANK: playback completed")
                }

                prepareAsync()
            }
            mediaPlayer = player
        } catch (e: Exception) {
            Log.e("PRANK", "PRANK: playback error = ${e.message}", e)
        }
    }

    @Synchronized
    fun stopAudio() {
        try {
            mediaPlayer?.let {
                if (it.isPlaying) {
                    it.stop()
                }
                it.reset()
                it.release()
            }
        } catch (e: Exception) {
            Log.e("PRANK", "PRANK: playback error stopping audio = ${e.message}", e)
        } finally {
            mediaPlayer = null
        }
    }
}
