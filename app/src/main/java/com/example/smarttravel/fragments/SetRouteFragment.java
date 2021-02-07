package com.example.smarttravel.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.smarttravel.Activities.MapActivity;
import com.example.smarttravel.R;

public class SetRouteFragment extends Fragment {

    MapActivity ma;
    TextView destination, startlocation, next;
    ImageView car, bike, walk;
    LinearLayout carLayout, bikeLayout, walkLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_set_route, container, false);
        ma = (MapActivity) getActivity();
        destination = root.findViewById(R.id.destination_txt);
        startlocation = root.findViewById(R.id.location_txt);
        next = root.findViewById(R.id.next_btn);
        car = root.findViewById(R.id.car_img);
        bike = root.findViewById(R.id.bike_img);
        walk = root.findViewById(R.id.walk_img);
        carLayout = root.findViewById(R.id.car_layout);
        bikeLayout = root.findViewById(R.id.bike_layout);
        walkLayout = root.findViewById(R.id.walk_layout);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        destination.setOnClickListener(view1 -> ma.searchLocation());

    }

    @Override
    public void onResume() {
        super.onResume();
        if (ma != null) {
            if (ma.placeName.length() > 0)
                destination.setText(ma.placeName);
        }
    }
}