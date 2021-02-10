package com.example.smarttravel.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.smarttravel.Activities.HomeActivity;
import com.example.smarttravel.Activities.LocalMusicActivity;
import com.example.smarttravel.R;

public class MusicFragment extends Fragment {

    RelativeLayout localMusic,spotify,jiosavan,gaana,youtube;
    HomeActivity homeActivity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.music_fragment,container,false);
        spotify = root.findViewById(R.id.spotifyRelativeLayout);
        localMusic = root.findViewById(R.id.localRelativeLayout);
        jiosavan = root.findViewById(R.id.jioRelativeLayout);
        gaana = root.findViewById(R.id.gaanaRelativeLaout);
        youtube = root.findViewById(R.id.youtubeRelativeLaout);
        homeActivity = (HomeActivity)getActivity();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        localMusic.setOnClickListener(view1 -> startActivity(new Intent(homeActivity, LocalMusicActivity.class)));

        spotify.setOnClickListener(view1 -> gotoUrl("https://open.spotify.com/?_gl=1*1m4d9i8*_gcl_aw*R0NMLjE2MTI4MjQzODUuQ2owS0NRaUEzNE9CQmhDY0FSSXNBRzMydXZNNjRTWnI3TXM2RmtJcjFiWmJoYnVUSGpLZmpSTmZ4NWxSUDlubGNRVzc4blc0SGFSa1JsQWFBcXU1RUFMd193Y0I.*_gcl_dc*R0NMLjE2MTI4MjQzODUuQ2owS0NRaUEzNE9CQmhDY0FSSXNBRzMydXZNNjRTWnI3TXM2RmtJcjFiWmJoYnVUSGpLZmpSTmZ4NWxSUDlubGNRVzc4blc0SGFSa1JsQWFBcXU1RUFMd193Y0I.&_ga=2.73061395.1231710312.1612824341-354077174.1612692103&_gac=1.14169157.1612824417.Cj0KCQiA34OBBhCcARIsAG32uvM64SZr7Ms6FkIr1bZbhbuTHjKfjRNfx5lRP9nlcQW78nW4HaRkRlAaAqu5EALw_wcB")
            );

        jiosavan.setOnClickListener(view12 -> gotoUrl("https://www.jiosaavn.com/"));

        gaana.setOnClickListener(view13 -> gotoUrl("https://gaana.com/"));

        youtube.setOnClickListener(view14 -> gotoUrl("https://music.youtube.com/"));
    }

    public void gotoUrl(String s){

        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));

    }
}
