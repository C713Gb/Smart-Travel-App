package com.example.smarttravel.Fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarttravel.Activities.HomeActivity;
import com.example.smarttravel.Activities.LocalMusicActivity;
import com.example.smarttravel.Adapters.RidesAdapter;
import com.example.smarttravel.Models.Route;
import com.example.smarttravel.R;
import com.example.smarttravel.SharedPreference.SharedPreference;
import com.example.smarttravel.Utils.Common;
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

    TextView create, greetings, songTxt, rides;
    HomeActivity homeActivity;
    DatabaseReference reference;
    ProgressDialog progressDialog;
    RelativeLayout relativeLayout, relativeLayout2;
    ImageView sponsored;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.home_fragment,container,false);
        create = root.findViewById(R.id.create_btn);
        greetings = root.findViewById(R.id.greetings_txt);
        songTxt = root.findViewById(R.id.song_txt);
        relativeLayout = root.findViewById(R.id.r_layout_2);
        relativeLayout2 = root.findViewById(R.id.r_layout_3);
        rides = root.findViewById(R.id.rides_txt);
        sponsored = root.findViewById(R.id.sponsored_img);
        homeActivity = (HomeActivity) getActivity();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        create.setOnClickListener(view1 -> homeActivity.goToMaps());

        String firstName = SharedPreference.getUserName(getContext());
        if (firstName.contains(" ")){
            int pos = firstName.indexOf(" ");
            firstName = firstName.substring(0, pos);
        }
        greetings.setText(Common.Companion.getGreetingMessage()+", "+firstName+"!");

        relativeLayout.setOnClickListener(view1 -> startActivity(new Intent(homeActivity, LocalMusicActivity.class)));
        songTxt.setOnClickListener(view1 -> startActivity(new Intent(homeActivity, LocalMusicActivity.class)));
        relativeLayout2.setOnClickListener(view1 -> homeActivity.addFragment(new UpcomingRidesFragment(), true, "nobottomnav"));
        rides.setOnClickListener(view1 -> homeActivity.addFragment(new UpcomingRidesFragment(), true, "nobottomnav"));
        sponsored.setOnClickListener(view1 -> gotoUrl("https://www.tesla.com/"));
    }

    public void gotoUrl(String s){

        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));

    }

}
