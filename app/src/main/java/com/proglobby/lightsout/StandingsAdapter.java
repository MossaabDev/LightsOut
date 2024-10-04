package com.proglobby.lightsout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StandingsAdapter extends RecyclerView.Adapter<StandingsAdapter.DriverHolder> {

    private List<Driver> drivers = new ArrayList<>();
    Context context;

    public StandingsAdapter(List<Driver> drivers) {
        this.drivers = drivers;
        sortDrivers(this.drivers);
    }

    @NonNull
    @Override
    public DriverHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leaderboard, parent, false);
        DriverHolder holder = new DriverHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DriverHolder holder, int position) {
        Driver driver = drivers.get(position);
        holder.nameText.setText(driver.getName());
        holder.teamText.setText(driver.getTeam());
        holder.numberText.setText(String.valueOf(position+1));
        holder.timeText.setText(driver.getTime());
        int id = context.getResources().getIdentifier(driver.getImg(), "drawable", context.getPackageName());
        holder.imageView.setImageResource(id);
    }

    @Override
    public int getItemCount() {
        return drivers.size();
    }

    public class DriverHolder extends RecyclerView.ViewHolder{
        TextView nameText;
        TextView timeText;
        ImageView imageView;
        TextView numberText;

        TextView teamText;

        public DriverHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.driver_name);
            timeText = itemView.findViewById(R.id.race_time);
            imageView = itemView.findViewById(R.id.driver_image);
            numberText = itemView.findViewById(R.id.position);
            teamText = itemView.findViewById(R.id.driver_team);
        }
    }

    public List<Driver> getDrivers() {
        return drivers;
    }

    public void setDrivers(List<Driver> drivers) {
        this.drivers = drivers;
    }

    public void sortDrivers(List<Driver> drivers){
        Collections.sort(drivers, new Comparator<Driver>() {
            @Override
            public int compare(Driver d1, Driver d2) {
                // Parse time strings into seconds and millis
                double time1 = parseTime(d1.getTime());
                double time2 = parseTime(d2.getTime());

                return Double.compare(time1, time2); // Compare the parsed times
            }
        });
    }

    private static double parseTime(String time) {
        if (time.equals("No Time Set")){
            return 9999999.999;
        }else{
            String[] parts = time.split(":");
            int seconds = Integer.parseInt(parts[0]);
            int millis = Integer.parseInt(parts[1]);
            return seconds + millis / 1000.0; // Convert millis to fractional seconds
        }
    }
}
