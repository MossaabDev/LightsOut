package com.proglobby.lightsout

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class StartingGridActivity : AppCompatActivity() {
    lateinit var clutchButton: ImageButton
    lateinit var instructionText: TextView
    lateinit var progressBar: ProgressBar
    lateinit var tableRow: TableRow
    lateinit var timeText:TextView
    var isTiming = false
    var isCounting = false

    var startTime: Long = 0

    //runs without a timer by reposting this handler at the end of the runnable
    var timerHandler = Handler()
    var timerRunnable: Runnable = object : Runnable {
        override fun run() {
            val millis = System.currentTimeMillis() - startTime
            var seconds = (millis / 1000).toInt()
            val minutes = seconds / 60
            seconds = seconds % 60
            timeText.setText(String.format("%d:%02d:%03d", minutes, seconds, millis % 1000))
            timerHandler.postDelayed(this, 20)
        }
    }
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starting_grid)
        clutchButton = findViewById(R.id.clutchButton)
        instructionText = findViewById(R.id.instructionText)
        progressBar = findViewById(R.id.progressBar)
        tableRow = findViewById(R.id.lights)
        timeText = findViewById(R.id.timer)
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
                    timeText.visibility = View.INVISIBLE
                    instructionText.visibility = View.VISIBLE
                    if (!isCounting){
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
                                    start = startLights()
                                    startCounting()
                                }


                            }
                            thread?.start()
                        }
                    }

                    true
                }

                MotionEvent.ACTION_UP -> {
                    // Stop the thread when button is released
                    timerHandler.removeCallbacks(timerRunnable);
                    end = System.currentTimeMillis()
                    if (progressBar.progress != 100){
                        start = 0
                    }
                    isRunning = false
                    if (start == -1.toLong()){
                        Toast.makeText(applicationContext, "Jump Start", Toast.LENGTH_SHORT).show()
                    }else{
                        if (start != 0.toLong() && start != (-1).toLong()){
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
    val startCounting = Thread {
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
    }

    fun startLights() : Long{
        //generate random value between 1 and 5
        isCounting = true
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
        isCounting = false
        return System.currentTimeMillis()
    }

    fun startCounting(){
        isTiming = true
        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);
        runOnUiThread(Runnable {
            timeText.visibility = View.VISIBLE
            instructionText.visibility = View.INVISIBLE
        })

    }

    fun stopCounting(){
        isTiming = false
        timerHandler.removeCallbacks(timerRunnable);

    }


}