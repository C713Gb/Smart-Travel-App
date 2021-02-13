package com.example.smarttravel.Fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarttravel.Activities.HomeActivity;
import com.example.smarttravel.Adapters.RidesAdapter;
import com.example.smarttravel.Models.Route;
import com.example.smarttravel.R;
import com.example.smarttravel.SharedPreference.SharedPreference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;

public class HomeFragment extends Fragment {

    TextView create;
    HomeActivity homeActivity;
    DatabaseReference reference;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.home_fragment,container,false);
        create = root.findViewById(R.id.create_btn);
        homeActivity = (HomeActivity) getActivity();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        create.setOnClickListener(view1 -> homeActivity.goToMaps());


    }

}
