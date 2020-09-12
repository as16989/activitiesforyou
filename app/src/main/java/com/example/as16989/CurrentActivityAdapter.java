package com.example.as16989;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CurrentActivityAdapter extends ArrayAdapter<CurrentActivityItem> {

    int mResource;

    public CurrentActivityAdapter(@NonNull Context context, int resource, @NonNull ArrayList<CurrentActivityItem> objects) {
        super(context, resource, objects);
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        int img = getItem(position).getImageResource();
        String heading = getItem(position).getHeading();
        String desc = getItem(position).getDesc();
        String start = getItem(position).getStart();

        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(mResource, parent, false);

        ImageView homeOrBusy = convertView.findViewById(R.id.location);
        TextView tvHeading = convertView.findViewById(R.id.tab);
        TextView tvDesc = convertView.findViewById(R.id.description);
        TextView tvStart = convertView.findViewById(R.id.start);

        homeOrBusy.setImageResource(img);
        tvHeading.setText(heading);
        tvDesc.setText(desc);
        tvStart.setText(start);

        return convertView;

    }
}
