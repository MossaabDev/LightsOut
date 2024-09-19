package com.proglobby.lightsout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.VideoView

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)

        val videoView: VideoView = findViewById<VideoView>(R.id.videoView)
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