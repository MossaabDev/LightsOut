package com.proglobby.lightsout

import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.VideoView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.security.AccessController.getContext


class SplashScreenActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)

        val videoView: VideoView = findViewById<VideoView>(R.id.videoView)
        videoView.setAudioFocusRequest(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK)
        videoView.setVideoPath("android.resource://" + packageName + "/" + R.raw.f1proglobby)
        val params = videoView.layoutParams
        params.width = metrics.widthPixels
        params.height = metrics.heightPixels
        videoView.layoutParams = params
        Thread.sleep(1000)
        videoView.start()
        //start another activity after the video ends
        videoView.setOnCompletionListener {
            val intent = Intent(this, StartingGridActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}