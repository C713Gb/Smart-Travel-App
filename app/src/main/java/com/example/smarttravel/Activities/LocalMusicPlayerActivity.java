package com.example.smarttravel.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.smarttravel.R;

import java.io.File;
import java.util.ArrayList;

public class LocalMusicPlayerActivity extends AppCompatActivity {

   /* Button pause,next,previous;
    SeekBar seekbar;
    TextView songText;
    String sName;

    static MediaPlayer myMediaPlayer;
    int position;
    ArrayList<String> mySongs;
    Thread updateSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_music_player);

        next = findViewById(R.id.nextButton);
        pause = findViewById(R.id.pauseButton);
        previous= findViewById(R.id.previousButton);
        songText = (TextView) findViewById(R.id.songName);
        seekbar = (SeekBar) findViewById(R.id.musicSeekBar);

        updateSeekBar = new Thread(){
            @Override
            public void run(){
                int totalDuration = myMediaPlayer.getDuration();
                int currentPosition= 0;

                while(currentPosition<totalDuration)
                {
                    try {
                        sleep(500);
                        currentPosition = myMediaPlayer.getCurrentPosition();
                        seekbar.setProgress(currentPosition);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        };

        if(myMediaPlayer!=null){
            myMediaPlayer.stop();
            myMediaPlayer.release();
        }

        Intent i =getIntent();
        Bundle bundel = i.getExtras();

        mySongs= (ArrayList) bundel.getParcelableArrayList("songs");

     //   sName= mySongs.get(position).getName().toString();

        String songName = i.getStringExtra("songname");

        songText.setText(songName);
        songText.setSelected(true);

        position = bundel.getInt("pos",0);

        Uri u = Uri.parse(mySongs.get(position).toString());

        myMediaPlayer = MediaPlayer.create(getApplicationContext(),u);

        myMediaPlayer.start();
        seekbar.setMax(myMediaPlayer.getDuration());

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                myMediaPlayer.seekTo(seekBar.getProgress());

            }
        });


    }*/
}