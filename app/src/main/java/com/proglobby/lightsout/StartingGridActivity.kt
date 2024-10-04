package com.proglobby.lightsout

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.media.SoundPool
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
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class StartingGridActivity : AppCompatActivity() {
    lateinit var clutchButton: ImageButton
    lateinit var instructionText: TextView
    lateinit var progressBar: ProgressBar
    lateinit var tableRow: TableRow
    lateinit var timeText: TextView
    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    var isTiming = false
    var isCounting = false
    var isJumpStart = false
    lateinit var soundPool: SoundPool
    var soundId: Int = 0

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
        navigationView = findViewById(R.id.navigationView)
        drawerLayout = findViewById(R.id.drawerLayout)
        clutchButton = findViewById(R.id.clutchButton)
        instructionText = findViewById(R.id.instructionText)
        progressBar = findViewById(R.id.progressBar)
        tableRow = findViewById(R.id.lights)
        timeText = findViewById(R.id.timer)
        val toolbar: Toolbar = findViewById(R.id.toolBar)
        val helper = DBHelper(applicationContext)
        if (helper.userInfos == null) {
            helper.initUser(User("Player", "", "driver1", "No Time Set"))
        }
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        val drawerToggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.leaderboard -> {
                    val intent = Intent(this, LeaderboardActivity::class.java)
                    startActivity(intent)
                }

                R.id.aboutUs -> {
                    Toast.makeText(applicationContext, "Settings", Toast.LENGTH_SHORT).show()
                }

                R.id.report -> {
                    Toast.makeText(applicationContext, "About", Toast.LENGTH_SHORT).show()
                }
            }
            drawerLayout.closeDrawers()
            true
        }

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

        soundPool = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            val audioAttributes = android.media.AudioAttributes.Builder()
                .setContentType(android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(android.media.AudioAttributes.USAGE_GAME)
                .build()
            android.media.SoundPool.Builder()
                .setMaxStreams(1)
                .setAudioAttributes(audioAttributes)
                .build()
        } else {
            android.media.SoundPool(1, android.media.AudioManager.STREAM_MUSIC, 0)
        }

        soundId = soundPool.load(this, R.raw.beep, 1)

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
                    instructionText.setTextColor(Color.BLACK)
                    instructionText.setText("Hold The Clutch")
                    if (!isCounting){
                        if (thread == null || !isRunning) {
                            isJumpStart = false
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
                                    if (!isJumpStart){
                                        startCounting()
                                    }

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
                        instructionText.setText("Jump Start")
                        instructionText.setTextColor(Color.RED)
                        isJumpStart = true

                    }else{
                        if (start != 0.toLong() && start != (-1).toLong()){
                            //Toast.makeText(applicationContext, "Time is ${end-start} millis", Toast.LENGTH_SHORT).show()
                            //get time in millis from the timeText
                            val time = timeText.text.toString()
                            if (time != "") {
                                val timeArray = time.split(":")
                                val minutes = timeArray[0].toInt()
                                val seconds = timeArray[1].toInt()
                                val millis = timeArray[2].toInt()
                                val timeInMillis = (minutes * 60 * 1000) + (seconds * 1000) + millis
                                val user = helper.userInfos
                                if (user != null){
                                    if (user.time.equals("No Time Set") || timeInMillis < user.timeInMillis){
                                        helper.updateUserTime(timeInMillis)
                                    }
                                }
                                if (timeInMillis < 300){
                                    Toast.makeText(applicationContext, "Great", Toast.LENGTH_SHORT).show()
                                   timeText.setTextColor(Color.GREEN)
                                }else if (timeInMillis < 500){
                                    //orange
                                    timeText.setTextColor(Color.argb(255, 255, 165, 0))
                                }else{
                                    timeText.setTextColor(Color.RED)
                                }

                            }

                        }
                    }
                    progressBar.progress = 0
                    true
                }

                else -> false
            }
        }

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawers()
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }
    val startCounting = Thread {
        //generate random value between float 1.0 and 5.0
        val randomValue = (1000..5000).random()
        //change the src of each of the image buttons in the table row into red

        for (i in 0 until tableRow.childCount){
            val image = tableRow.getChildAt(i) as ImageView
            Thread.sleep(1000)
            image.setImageResource(R.drawable.red)
        }
        //change all the imageViews again to gray after random sconds
        Thread.sleep(randomValue.toLong())
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
        soundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f)
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