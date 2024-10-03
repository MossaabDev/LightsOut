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
        Collections.sort(drivers, Comparator.comparing(Driver::getTime));
    }
}
