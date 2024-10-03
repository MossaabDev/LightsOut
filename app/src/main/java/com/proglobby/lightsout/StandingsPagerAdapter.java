package com.proglobby.lightsout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.invoke.CallSite;
import java.util.ArrayList;
import java.util.List;

public class StandingsPagerAdapter extends RecyclerView.Adapter<StandingsPagerAdapter.PageHolder>{

    List<Page> pages = new ArrayList<>();
    Context context;
    public StandingsPagerAdapter(List<Page> pages, Context context) {
        this.pages = pages;
        this.context = context;
    }

    public List<Page> getPages() {
        return pages;
    }

    public void setPages(List<Page> pages) {
        this.pages = pages;
    }

    @NonNull
    @Override
    public PageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.standings_page, parent, false);
        PageHolder pageHolder = new PageHolder(view);
        return pageHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PageHolder holder, int position) {
        Page page = pages.get(position);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(context);
        StandingsAdapter adapter = new StandingsAdapter(page.getDriverList());
        holder.recyclerView.setLayoutManager(lm);
        holder.recyclerView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return pages.size();
    }

    public class PageHolder extends RecyclerView.ViewHolder{
        RecyclerView recyclerView;


        public PageHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recycler_view_leaderboard);
        }
    }
}
