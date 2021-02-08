package com.example.smarttravel.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarttravel.Activities.HomeActivity;
import com.example.smarttravel.Activities.MapActivity;
import com.example.smarttravel.R;

public class HomeFragment extends Fragment {

    TextView create;
    RecyclerView upcoming, past;
    HomeActivity homeActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.home_fragment,container,false);
        create = root.findViewById(R.id.create_btn);
        upcoming = root.findViewById(R.id.upcoming_routes);
        past = root.findViewById(R.id.past_routes);
        homeActivity = (HomeActivity) getActivity();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        create.setOnClickListener(view1 -> homeActivity.goToMaps());
    }
}
