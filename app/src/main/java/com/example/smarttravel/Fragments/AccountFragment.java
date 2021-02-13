package com.example.smarttravel.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.smarttravel.Activities.HomeActivity;
import com.example.smarttravel.R;
import com.example.smarttravel.SharedPreference.SharedPreference;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountFragment extends Fragment {

    HomeActivity homeActivity;
    TextView username, email, editProfile, upcomingRides, pastRides;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    CircleImageView profileImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.account_fragment,container,false);

        homeActivity = (HomeActivity)getActivity();
        email = root.findViewById(R.id.email_txt);
        username = root.findViewById(R.id.username_txt);
        editProfile = root.findViewById(R.id.edit_btn);
        upcomingRides = root.findViewById(R.id.rides_btn);
        pastRides = root.findViewById(R.id.rides_btn2);
        profileImage = root.findViewById(R.id.circle_image);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        username.setText(SharedPreference.getUserName(getContext()));
        email.setText(SharedPreference.getUserEmail(getContext()));

        if (!SharedPreference.getUserPic(getContext()).equals("default")){
            Glide.with(getContext())
                    .load(SharedPreference.getUserPic(getContext()))
                    .into(profileImage);
        }

        editProfile.setOnClickListener(view1 -> {
            homeActivity.addFragment(new EditProfileFragment(), true, "nobottomnav");
        });

        upcomingRides.setOnClickListener(view1 -> {

        });

        pastRides.setOnClickListener(view1 -> {

        });

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        }


    }
}
