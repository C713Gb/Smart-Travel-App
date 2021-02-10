package com.example.smarttravel.Activities;

import android.Manifest;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.smarttravel.R;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_music);

        if(ContextCompat.checkSelfPermission(LocalMusicActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(LocalMusicActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)){

                ActivityCompat.requestPermissions(LocalMusicActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},My_Permisson);

            }
            else{
                ActivityCompat.requestPermissions(LocalMusicActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},My_Permisson);
            }
        }
        else if(ContextCompat.checkSelfPermission(LocalMusicActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(LocalMusicActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){

                ActivityCompat.requestPermissions(LocalMusicActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},My_Permissions);

            }
            else{
                ActivityCompat.requestPermissions(LocalMusicActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},My_Permissions);
            }
        }
        else{
            doStuff();
        }

    }
    public void doStuff(){
        songListView = (ListView) findViewById(R.id.songlistView);
        arrayList = new ArrayList<>();

        getMusic(); // initialize Uri here

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        songListView.setAdapter(arrayAdapter);

        songListView.setOnItemClickListener((adapterView, view, i, l) -> {

            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioAttributes(
                    new AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
            );
            try {
                mediaPlayer.setDataSource(files.get(i).toString());
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (Exception e) {
                e.printStackTrace();
            }

            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.start();
            Toast.makeText(LocalMusicActivity.this, "hi Viswesh Lalli", Toast.LENGTH_SHORT).show();
        });
    }
    void getMusic(){
        files = new ArrayList<>();
        ContentResolver contentResolver = getContentResolver();
        Uri songuri=MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songcursor = contentResolver.query(songuri,null,null,null,null);

        if(songcursor!=null && songcursor.moveToFirst()){
            int songTitle = songcursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist= songcursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);

            do{
                String currentTitle = songcursor.getString(songTitle);
                String currentArtist= songcursor.getString(songArtist);
                arrayList.add(currentTitle + "\n" +currentArtist);
                files.add((new File(songcursor.getString(songcursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)))));
            } while (songcursor.moveToNext());
        }

        Log.d("TAG", "getMusic: Song path "+songuri.getPath());

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case My_Permisson:{
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(LocalMusicActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
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
                    if(ContextCompat.checkSelfPermission(LocalMusicActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
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