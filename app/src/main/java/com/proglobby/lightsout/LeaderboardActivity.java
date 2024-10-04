package com.proglobby.lightsout;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardActivity extends Activity {
    ViewPager2 viewPager;
    TextView fastestTime;

    Button f1GridBtn;
    Button globalBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        viewPager = findViewById(R.id.viewPager);
        fastestTime = findViewById(R.id.fastestTime);
        f1GridBtn = findViewById(R.id.f1_grid_button);
        globalBtn = findViewById(R.id.global_button);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        List<Driver> f1Drivers = new ArrayList<>();
        f1Drivers.add(new Driver("Charles Leclerc", "Ferrari", "leclerc", "0:219"));
        f1Drivers.add(new Driver("Lewis Hamilton", "Mercides", "hamilton", "0:202"));
        f1Drivers.add(new Driver("Valteri Bottas", "Mercedes", "bottas", "0:048"));
        f1Drivers.add(new Driver("Michael Schumacher", "Ferrari", "schumi", "0:145"));
        f1Drivers.add(new Driver("Ayrton Senna", "Mclaren", "senna", "0:133"));
        f1Drivers.add(new Driver("Max Verstappen", "RedBull Racing", "verstappen", "0:189"));
        f1Drivers.add(new Driver("Sebastian Vettel", "Ferrari", "vettel", "0:200"));

        DBHelper helper = new DBHelper(this);


        if (helper.getDrivers().size() == 0){
            helper.initScoreTable(f1Drivers);
            System.out.println("problem");
        }else{
            helper.updateDrivers(f1Drivers);
        }
        f1Drivers = helper.getDrivers();
        User user = helper.getUserInfos();
        fastestTime.setText(user.getTime());
        f1Drivers.add(user);


        List<Page> pages = new ArrayList<>();
        pages.add(new Page(f1Drivers));
        Page page = new Page(new ArrayList<Driver>());
        page.isReady = false;
        pages.add(page);
        StandingsPagerAdapter adapter = new StandingsPagerAdapter(pages, this);
        viewPager.setAdapter(adapter);
        //listener of the viewpager
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 0){
                    //change the drawable of the button
                    f1GridBtn.setBackground(getDrawable(R.drawable.red_btn_bg));
                    globalBtn.setBackground(getDrawable(R.drawable.gray_btn_bg));
                }else{
                    f1GridBtn.setBackground(getDrawable(R.drawable.gray_btn_bg));
                    globalBtn.setBackground(getDrawable(R.drawable.red_btn_bg));
                }
            }
        });

    }
}
