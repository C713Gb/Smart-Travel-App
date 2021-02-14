package com.example.smarttravel.Fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.smarttravel.Activities.HomeActivity;
import com.example.smarttravel.Activities.MapActivity;
import com.example.smarttravel.Activities.WelcomeActivity;
import com.example.smarttravel.R;
import com.example.smarttravel.SharedPreference.SharedPreference;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountFragment extends Fragment {

    HomeActivity homeActivity;
    TextView username, email, editProfile, upcomingRides, pastRides, logout;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    CircleImageView profileImage;
    ImageView next1, next2, next3, next4;
    Dialog logoutDialog;
    View logoutView;

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
        next1 = root.findViewById(R.id.next_edit);
        next2 = root.findViewById(R.id.next_upcoming);
        next3 = root.findViewById(R.id.next_past);
        next4 = root.findViewById(R.id.next_log_out);
        logout = root.findViewById(R.id.log_out_btn);
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

        logoutDialog = new Dialog(getContext());
        logoutDialog.setContentView(R.layout.log_out_dialog);
        logoutDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        logoutDialog.setCanceledOnTouchOutside(false);
        logoutDialog.setCancelable(false);

        TextView yes = logoutDialog.findViewById(R.id.yes_btn);
        TextView no = logoutDialog.findViewById(R.id.no_btn);

        yes.setOnClickListener(view1 -> {
            logoutDialog.dismiss();
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(homeActivity, WelcomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            homeActivity.finish();
        });

        no.setOnClickListener(view1 -> logoutDialog.dismiss());

        logout.setOnClickListener(view1 -> logoutDialog.show());

        next4.setOnClickListener(view1 -> logoutDialog.show());

        next1.setOnClickListener(view1 -> {
            homeActivity.addFragment(new EditProfileFragment(), true, "nobottomnav");
        });

        next2.setOnClickListener(view1 -> {
            homeActivity.addFragment(new UpcomingRidesFragment(), true, "nobottomnav");
        });

        next3.setOnClickListener(view1 -> {
            homeActivity.addFragment(new PastRidesFragment(), true, "nobottomnav");
        });

        editProfile.setOnClickListener(view1 -> {
            homeActivity.addFragment(new EditProfileFragment(), true, "nobottomnav");
        });

        upcomingRides.setOnClickListener(view1 -> {
            homeActivity.addFragment(new UpcomingRidesFragment(), true, "nobottomnav");
        });

        pastRides.setOnClickListener(view1 -> {
            homeActivity.addFragment(new PastRidesFragment(), true, "nobottomnav");
        });

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        }


    }
}
