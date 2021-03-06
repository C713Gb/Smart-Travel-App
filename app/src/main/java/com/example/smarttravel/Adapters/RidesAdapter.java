package com.example.smarttravel.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarttravel.Activities.HomeActivity;
import com.example.smarttravel.Models.Route;
import com.example.smarttravel.R;

import java.util.List;

public class RidesAdapter extends RecyclerView.Adapter<RidesAdapter.ViewHolder> {

    Context mContext;
    List<Route> routeList;
    HomeActivity homeActivity;

    public RidesAdapter(Context mContext, List<Route> routeList) {
        this.mContext = mContext;
        this.routeList = routeList;
    }

    @NonNull
    @Override
    public RidesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.ride_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RidesAdapter.ViewHolder holder, int position) {

        homeActivity = (HomeActivity) mContext;
        holder.place.setText(routeList.get(position).getDestination());
        holder.date.setText(routeList.get(position).getDate());

        holder.place.setOnClickListener(view -> homeActivity.updateMap(routeList.get(position)));

    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView date, place;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date_item);
            place = itemView.findViewById(R.id.place_item);
        }
    }
}
