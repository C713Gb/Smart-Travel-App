package com.example.smarttravel.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.smarttravel.Activities.LocalMusicActivity;
import com.example.smarttravel.R;

public class MusicPlayerFragment extends Fragment {

    TextView title;
    ImageView play, pause, next;
    SeekBar seekBar;
    LocalMusicActivity localMusicActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_music__player, container, false);
        title = root.findViewById(R.id.play_title);
        play = root.findViewById(R.id.play);
        pause = root.findViewById(R.id.pause);
        next = root.findViewById(R.id.play_next);
        seekBar = root.findViewById(R.id.progress_seek_bar);
        localMusicActivity = (LocalMusicActivity) getActivity();
        return root;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        seekBar.setOnTouchListener((view1, motionEvent) -> false);

        play.setOnClickListener(view1 -> {
            if (localMusicActivity.playSong()){
                play.setVisibility(View.GONE);
                pause.setVisibility(View.VISIBLE);
            }
        });

        pause.setOnClickListener(view1 -> {
            if (localMusicActivity.pauseSong()){
                play.setVisibility(View.VISIBLE);
                pause.setVisibility(View.GONE);
            }
        });

        next.setOnClickListener(view1 -> nextClick());
    }

    public void updateSeekBar(){

        new Handler().postDelayed(() -> {
            seekBar.setMax(localMusicActivity.mediaPlayer.getDuration()/1000);

            Handler handler = new Handler();
            localMusicActivity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if(localMusicActivity.mediaPlayer != null && localMusicActivity.mediaPlayer.isPlaying() ){
                        int mCurrentPosition = localMusicActivity.mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this, 1000);
                }
            });
        }, 250);

    }

    public void nextClick(){
        localMusicActivity.nextSong();
        play.setVisibility(View.GONE);
        pause.setVisibility(View.VISIBLE);
        seekBar.setMax(localMusicActivity.mediaPlayer.getDuration()/1000);

        Handler handler = new Handler();
        localMusicActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if(localMusicActivity.mediaPlayer != null){
                    int mCurrentPosition = localMusicActivity.mediaPlayer.getCurrentPosition() / 1000;
                    seekBar.setProgress(mCurrentPosition);
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

    public void updateTitle(String strTitle){

        new Handler().postDelayed(() -> {
            String[] strings = strTitle.split("\n");
            title.setText(strings[1]);
        }, 250);

    }
}