package com.proglobby.lightsout;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Calendar;

public class Counter extends ViewModel {
    //create a counter with minutes seconds and milliseconds
    private int minutes = 0;
    private int seconds = 0;
    private int milliseconds = 0;

    MutableLiveData<String> time = new MutableLiveData<>();

    public void initTimer(){
        minutes = 0;
        seconds = 0;
        milliseconds = 0;
        time.setValue("00:00:000");
    }

    public void startTimer(){

        //start the timer
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){

                    milliseconds++;
                    if (milliseconds == 1000){
                        milliseconds = 0;
                        seconds++;
                    }
                    if (seconds == 60){
                        seconds = 0;
                        minutes++;
                    }
                    time.setValue(minutes + ":" + seconds + ":" + milliseconds);

                }
            }
        }).start();
    }
}
