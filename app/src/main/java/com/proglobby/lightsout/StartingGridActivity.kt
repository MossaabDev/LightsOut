package com.proglobby.lightsout

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast

class StartingGridActivity : AppCompatActivity() {
    lateinit var clutchButton: ImageButton
    lateinit var instructionText: TextView
    lateinit var progressBar: ProgressBar
    lateinit var tableRow: TableRow
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starting_grid)
        clutchButton = findViewById(R.id.clutchButton)
        instructionText = findViewById(R.id.instructionText)
        progressBar = findViewById(R.id.progressBar)
        tableRow = findViewById(R.id.lights)
        val thread1: Thread = object : Thread() {
            override fun run() {
                try {
                    while (progressBar.progress < 100) {
                        sleep(100)
                        progressBar.progress += 1
                    }
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }

        val runnable = object : Runnable{
            override fun run() {
                try {
                    while (progressBar.progress < 100) {
                        Thread.sleep(100)
                        progressBar.progress += 1
                    }
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }

        }

        val handler = Handler(Looper.getMainLooper())

        var thread: Thread? = null
        var isRunning = false
        var start: Long = 0;
        var end: Long = 0;
        clutchButton.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // Start the thread to increase progress
                    if (thread == null || !isRunning) {
                        isRunning = true
                        start = -1
                        thread = Thread {
                            while (progressBar.progress < 100 && isRunning) {
                                // Increment the progress
                                // Update the ProgressBar on the main thread
                                runOnUiThread {
                                    progressBar.progress = progressBar.progress + 1
                                }

                                // Sleep for a short time to simulate continuous progress
                                Thread.sleep(10)
                            }
                            if (isRunning){
                                start = startCounting()
                            }


                        }
                        thread?.start()
                    }
                    true
                }

                MotionEvent.ACTION_UP -> {
                    // Stop the thread when button is released
                    end = System.currentTimeMillis()
                    if (progressBar.progress != 100){
                        start = 0
                    }
                    isRunning = false
                    if (start == -1.toLong()){
                        Toast.makeText(applicationContext, "Jump Start", Toast.LENGTH_SHORT).show()
                    }else{
                        if (start != 0.toLong()){
                            Toast.makeText(applicationContext, "Time is ${end-start} millis", Toast.LENGTH_SHORT).show()
                        }
                    }
                    progressBar.progress = 0
                    true
                }

                else -> false
            }
        }
    }

    fun startCounting() : Long{
        //generate random value between 1 and 5
        val randomValue = (1..5).random()
        //change the src of each of the image buttons in the table row into red

        for (i in 0 until tableRow.childCount){
            val image = tableRow.getChildAt(i) as ImageView
            Thread.sleep(1000)
            image.setImageResource(R.drawable.red)
        }
        //change all the imageViews again to gray after random sconds
        Thread.sleep(randomValue * 1000L)
        for (i in 0 until tableRow.childCount){
            val image = tableRow.getChildAt(i) as ImageView
            image.setImageResource(R.drawable.gray)
        }
        return System.currentTimeMillis()
    }
}