package com.example.as16989;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.LogVH> {

    private  ArrayList<LogItem> mLog;

    public static class LogVH extends RecyclerView.ViewHolder {

        public ImageView emoji;
        public TextView start, end, heading;

        public LogVH(@NonNull View itemView) {
            super(itemView);
            emoji = itemView.findViewById(R.id.emoji);
            start = itemView.findViewById(R.id.started);
            end = itemView.findViewById(R.id.ended);
            heading = itemView.findViewById(R.id.heading);
        }
    }

    public LogAdapter(ArrayList<LogItem> log) {
        mLog = log;
    }

    @NonNull
    @Override
    public LogVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        LogVH lvh = new LogVH(v);
        return lvh;
    }

    @Override
    public void onBindViewHolder(@NonNull LogVH holder, int position) {
        LogItem currentItem = mLog.get(position);
        holder.emoji.setImageResource(currentItem.getImageResource());
        holder.start.setText(currentItem.getStart());
        holder.end.setText(currentItem.getEnd());
        holder.heading.setText(currentItem.getHeading());
    }

    @Override
    public int getItemCount() {
        return mLog.size();
    }



}
