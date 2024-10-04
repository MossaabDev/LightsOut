package com.proglobby.lightsout;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardActivity extends Activity {
    ViewPager2 viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        viewPager = findViewById(R.id.viewPager);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        List<Driver> f1Drivers = new ArrayList<>();
        f1Drivers.add(new Driver("Charles Leclerc", "Ferrari", "leclerc", "0:219"));
        f1Drivers.add(new Driver("Lewis Hamilton", "Mercides", "hamilton", "0:168"));
        f1Drivers.add(new Driver("Valteri Bottas", "Kick Sauber", "bottas", "0:084"));
        DBHelper helper = new DBHelper(this);

        List<Page> pages = new ArrayList<>();
        pages.add(new Page(f1Drivers));
        StandingsPagerAdapter adapter = new StandingsPagerAdapter(pages, this);
        viewPager.setAdapter(adapter);

    }
}
