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
    RecyclerView upcoming, past;
    HomeActivity homeActivity;
    DatabaseReference reference;
    List<Route> pastRouteList, upcomingRouteList;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.home_fragment,container,false);
        create = root.findViewById(R.id.create_btn);
        upcoming = root.findViewById(R.id.upcoming_routes);
        past = root.findViewById(R.id.past_routes);
        homeActivity = (HomeActivity) getActivity();
        pastRouteList = new ArrayList<>();
        upcomingRouteList = new ArrayList<>();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        updateRides();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        create.setOnClickListener(view1 -> homeActivity.goToMaps());

        past.setHasFixedSize(true);
        upcoming.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext());
        linearLayoutManager2.setReverseLayout(true);
        linearLayoutManager2.setStackFromEnd(true);

        past.setLayoutManager(linearLayoutManager);
        upcoming.setLayoutManager(linearLayoutManager2);


    }

    public void updateRides() {
        progressDialog.setMessage("Loading Rides");
        progressDialog.show();
        upcomingRouteList.clear();
        pastRouteList.clear();

        try {
            reference = FirebaseDatabase.getInstance().getReference("rides");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Route route = snapshot.getValue(Route.class);
                        String userId = SharedPreference.getUserId(getContext());
                        if (userId.equals(route.getUserId())) {

                            Date c = Calendar.getInstance().getTime();
                            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                            String currentDate = df.format(c);

                            @SuppressLint("SimpleDateFormat")
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                            Date strDate = null;
                            try {
                                strDate = sdf.parse(currentDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            String check = route.getDate();
                            Date date = null;
                            try {
                                date = sdf.parse(check);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            if (date.after(strDate)) {
                                upcomingRouteList.add(route);
                            }else if (date.before(strDate)) {
                                pastRouteList.add(route);
                            }else{
                                upcomingRouteList.add(route);
                            }
                        }
                    }
                    progressDialog.dismiss();

                    RidesAdapter ridesAdapter = new RidesAdapter(getContext(), pastRouteList);
                    past.setAdapter(ridesAdapter);

                    RidesAdapter ridesAdapter2 = new RidesAdapter(getContext(), upcomingRouteList);
                    upcoming.setAdapter(ridesAdapter2);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    progressDialog.dismiss();
                    Timber.d(error.getMessage());
                }
            });
        } catch (Exception e){
            progressDialog.dismiss();
            e.printStackTrace();
        }

    }
}
