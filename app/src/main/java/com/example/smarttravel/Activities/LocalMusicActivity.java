package com.example.smarttravel.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.smarttravel.Fragments.MusicPlayerFragment;
import com.example.smarttravel.Fragments.SetRouteFragment;
import com.example.smarttravel.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LocalMusicActivity extends AppCompatActivity {

    public static final int My_Permisson =1, My_Permissions = 2;
    private static final String TAG = "TAG";
    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;
    ListView songListView;
    List<File> files;
    BottomSheetBehavior bottomSheetBehavior;
    View bottomSheet;
    public MediaPlayer mediaPlayer;
    MusicPlayerFragment fragment;
    int songPosition = 0;
    ImageView back;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_music);
        bottomSheet = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        back = findViewById(R.id.back_btn);
        back.setOnClickListener(view -> onBackPressed());

        initializeMediaplayer();

        if(ContextCompat.checkSelfPermission(LocalMusicActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(LocalMusicActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},My_Permisson);
        }
        else if(ContextCompat.checkSelfPermission(LocalMusicActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(LocalMusicActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},My_Permissions);
        }
        else{
            doStuff();
        }



    }

    @Override
    public void onBackPressed() {
        mediaPlayer.stop();
        super.onBackPressed();
    }

    private void initializeMediaplayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
        );

        mediaPlayer.setOnCompletionListener(mediaPlayer -> {
            if (fragment != null){
                fragment.nextClick();
            }
        });
    }

    public void doStuff(){
        songListView = (ListView) findViewById(R.id.songlistView);
        arrayList = new ArrayList<>();

        getMusic(); // initialize Uri here

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        songListView.setAdapter(arrayAdapter);

        songListView.setOnItemClickListener((adapterView, view, i, l) -> {

            openMusic();
            closePreviousSong();
            updateFragment(arrayList.get(i));
            songPosition = i;

            try {
                mediaPlayer.setDataSource(files.get(i).toString());
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (Exception e) {
                e.printStackTrace();
            }

            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.start();

            if (fragment != null){
                fragment.updateSeekBar();
            }
        });
    }

    private void closePreviousSong() {
        mediaPlayer.reset();
    }

    public boolean playSong(){
        if (!mediaPlayer.isPlaying()){
            mediaPlayer.start();
            return true;
        } else{
            return false;
        }
    }

    public boolean pauseSong(){
        if (mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            return  true;
        } else{
            return  false;
        }
    }

    public void nextSong(){
        closePreviousSong();

        if (songPosition < arrayList.size()-1){
            try {
                mediaPlayer.setDataSource(files.get(++songPosition).toString());
                mediaPlayer.prepare();
                mediaPlayer.start();
                updateFragment(arrayList.get(songPosition));
            } catch (Exception e) {
                e.printStackTrace();
            }

            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.start();
        } else {
            try {
                mediaPlayer.setDataSource(files.get(0).toString());
                mediaPlayer.prepare();
                mediaPlayer.start();
                updateFragment(arrayList.get(0));
            } catch (Exception e) {
                e.printStackTrace();
            }

            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.start();
            songPosition = 0;
        }
    }

    private void updateFragment(String s) {
        if (fragment!= null){
            fragment.updateTitle(s);
        }else {
            Log.d(TAG, "updateFragment: NULL");
        }
    }

    void getMusic(){
        files = new ArrayList<>();
        ContentResolver contentResolver = getContentResolver();
        Uri songuri=MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songcursor = contentResolver
                .query(songuri,null,null,null,null);

        if(songcursor!=null && songcursor.moveToFirst()){
            int songTitle = songcursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist= songcursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);

            do{
                String currentTitle = songcursor.getString(songTitle);
                String currentArtist= songcursor.getString(songArtist);
                if (currentArtist.contains("unknown")) currentArtist = "Unknown Artist";
                arrayList.add("\n" + currentTitle + "\n" +currentArtist + "\n");
                files.add((new File(songcursor.getString(songcursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)))));
            } while (songcursor.moveToNext());
        }

        Log.d("TAG", "getMusic: Song path "+songuri.getPath());

    }

    private void openMusic(){
        fragment = new MusicPlayerFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.music_container,
                fragment).commit();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode){
            case My_Permisson:{
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(LocalMusicActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
                        doStuff();
                    }

                }else{
                    Toast.makeText(this, "Permission Not Granted", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }

            case My_Permissions:{
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(LocalMusicActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
                        doStuff();
                    }

                }else{
                    Toast.makeText(this, "Permission Not Granted", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

}