package com.example.smarttravel.Fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.Fragment;

import com.example.smarttravel.Activities.MapActivity;
import com.example.smarttravel.Models.Destination;
import com.example.smarttravel.Models.Route;
import com.example.smarttravel.R;
import com.example.smarttravel.SharedPreference.SharedPreference;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import timber.log.Timber;

public class SetRouteFragment extends Fragment {

    MapActivity ma;
    TextView destination, startlocation, next, date;
    ImageView car, bike, walk;
    LinearLayout carLayout, bikeLayout, walkLayout;
    String selectedMode = "";
    ProgressDialog progressDialog;
    Route route;

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
        date = root.findViewById(R.id.date_txt);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        destination.setOnClickListener(view1 -> ma.searchLocation());
        new Handler().postDelayed(() -> ma.bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED), 1000);

        if (ma.strUpdate.equals("true")){
            route = ma.route;
            String mode = route.getMode();
            if (mode.equals("car")) updateCar();
            else if (mode.equals("bike")) updateBike();
            else if (mode.equals("walk")) updateWalk();

            destination.setText(route.getDestination());
            date.setText(route.getDate());
            next.setText("Update");
            new Handler().postDelayed(() -> {
                ma.showDirection2(route.getDestinationLatLng());
            }, 2000);

        }

        car.setOnClickListener(view1 -> updateCar());

        bike.setOnClickListener(view1 -> updateBike());

        walk.setOnClickListener(view1 -> updateWalk());

        carLayout.setOnClickListener(view1 -> updateCar());

        bikeLayout.setOnClickListener(view1 -> updateBike());

        walkLayout.setOnClickListener(view1 -> updateWalk());

        date.setOnClickListener(view1 -> new DatePickerDialog(getContext(), ma.myDateListener, ma.calendar
                .get(Calendar.YEAR), ma.calendar.get(Calendar.MONTH),
                ma.calendar.get(Calendar.DAY_OF_MONTH)).show());

        next.setOnClickListener(view1 -> {
            if ((destination.getText().toString().length() > 0) &&
                    (!destination.getText().toString().equals("Select destination"))){
                if (selectedMode.length() > 0){
                    if (!date.getText().toString().equals("Set Travel Date")){
                        if (ma.strUpdate.equals("true")){
                            updateRoute();
                        } else {
                            updateDatabase();
                        }
                    } else {
                        Toast.makeText(ma, "Please select date", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ma, "Please select mode", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ma, "Please select destination", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void updateRoute() {
        progressDialog.setMessage("Updating...");
        progressDialog.show();

        try {

            Destination destinationObject = new Destination(Double.toString(ma.destination.latitude()),
                    Double.toString(ma.destination.longitude()));

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("rides");
            String id = route.getRouteId();

            Route route = new Route(
                    destination.getText().toString(),
                    SharedPreference.getUserId(getContext()),
                    selectedMode,
                    date.getText().toString(),
                    destinationObject,
                    id
            );

            assert id != null;
            reference.child(id).setValue(route).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(ma, "Ride successfully updated!", Toast.LENGTH_SHORT).show();
                    ma.goToHome();
                } else {
                    progressDialog.dismiss();
                    Timber.d("updateRoute: %s", task.getException().getMessage());
                    task.getException().printStackTrace();
                }
            });

        } catch (Exception e){
            Toast.makeText(ma, "Update Failed", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    private void updateDatabase() {
        progressDialog.setMessage("Uploading...");
        progressDialog.show();

        try {

            Destination destinationObject = new Destination(Double.toString(ma.destination.latitude()),
                    Double.toString(ma.destination.longitude()));

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("rides");
            String id = reference.push().getKey();

            Route route = new Route(
                    destination.getText().toString(),
                    SharedPreference.getUserId(getContext()),
                    selectedMode,
                    date.getText().toString(),
                    destinationObject,
                    id
            );

            assert id != null;
            reference.child(id).setValue(route).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(ma, "Ride successfully created!", Toast.LENGTH_SHORT).show();
                    ma.goToHome();
                } else {
                    progressDialog.dismiss();
                    Timber.d("updateDatabase: %s", task.getException().getMessage());
                    task.getException().printStackTrace();
                }
            });

        } catch (Exception e){
            progressDialog.dismiss();
            e.printStackTrace();
        }

    }

    private void updateWalk() {
        selectedMode = "walk";
        carLayout.setBackgroundResource(R.drawable.textview_background);
        bikeLayout.setBackgroundResource(R.drawable.textview_background);
        walkLayout.setBackgroundResource(R.drawable.black_background);
        ImageViewCompat.setImageTintList(car,
                ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.black)));
        ImageViewCompat.setImageTintList(bike,
                ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.black)));
        ImageViewCompat.setImageTintList(walk,
                ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.white)));
    }

    private void updateBike() {
        selectedMode = "bike";
        carLayout.setBackgroundResource(R.drawable.textview_background);
        bikeLayout.setBackgroundResource(R.drawable.black_background);
        walkLayout.setBackgroundResource(R.drawable.textview_background);
        ImageViewCompat.setImageTintList(car,
                ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.black)));
        ImageViewCompat.setImageTintList(bike,
                ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.white)));
        ImageViewCompat.setImageTintList(walk,
                ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.black)));
    }

    private void updateCar() {
        selectedMode = "car";
        carLayout.setBackgroundResource(R.drawable.black_background);
        bikeLayout.setBackgroundResource(R.drawable.textview_background);
        walkLayout.setBackgroundResource(R.drawable.textview_background);
        ImageViewCompat.setImageTintList(car,
                ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.white)));
        ImageViewCompat.setImageTintList(bike,
                ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.black)));
        ImageViewCompat.setImageTintList(walk,
                ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.black)));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ma != null) {
            if (ma.placeName.length() > 0)
                destination.setText(ma.placeName);
        }
    }

    public void updateDate(StringBuilder s){
        date.setText(s);
    }
}