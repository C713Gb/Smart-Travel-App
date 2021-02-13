package com.example.smarttravel.Activities;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.content.CursorLoader;

import com.example.smarttravel.Fragments.AccountFragment;
import com.example.smarttravel.Fragments.EditProfileFragment;
import com.example.smarttravel.Fragments.HomeFragment;
import com.example.smarttravel.Fragments.MusicFragment;
import com.example.smarttravel.Models.Route;
import com.example.smarttravel.Models.User;
import com.example.smarttravel.R;
import com.example.smarttravel.SharedPreference.SharedPreference;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.theartofdev.edmodo.cropper.CropImage;

import timber.log.Timber;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "TAG";
    public BottomNavigationView bottomFrag;
    DatabaseReference reference;
    FirebaseAuth auth;
    public String currentFrame = "";
    public Uri imageUri;
    public String myUrl="";

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null){
            startActivity(new Intent(HomeActivity.this, WelcomeActivity.class));
            finish();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        bottomFrag = findViewById(R.id.bottom_fragment_menu);
        bottomFrag.setOnNavigationItemSelectedListener(bfragListner);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new HomeFragment()).commit();

        if (auth.getCurrentUser() != null) updateSharedPreference();

    }

    public void updateSharedPreference() {
        String userId = auth.getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference("users").child(userId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                assert user != null;
                SharedPreference.setUserEmail(HomeActivity.this, user.getUserEmail());
                SharedPreference.setUserId(HomeActivity.this, user.getUserId());
                SharedPreference.setUserName(HomeActivity.this, user.getUsername());
                SharedPreference.setUserPic(HomeActivity.this, user.getUserPic());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Timber.d("onCancelled: %s", error.getMessage());
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    private final BottomNavigationView.OnNavigationItemSelectedListener bfragListner =
            item -> {
                Fragment selectedFragment = null;

                switch (item.getItemId()) {
                    case R.id.bf_home:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.bf_music:
                        selectedFragment = new MusicFragment();
                        break;
                    case R.id.bf_account:
                        selectedFragment = new AccountFragment();
                        break;
                }
                assert selectedFragment != null;
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, selectedFragment).commit();

                return true;
            };

    public void goToMaps() {
        startActivity(new Intent(HomeActivity.this, MapActivity.class));
    }

    public void updateMap(Route route){

        Gson gson = new Gson();
        String jsonString = gson.toJson(route);

        Intent intent = new Intent(HomeActivity.this, MapActivity.class);
        intent.putExtra("Route", jsonString);
        startActivity(intent);
    }

    public void addPicture(){
        CropImage.activity()
                .setAspectRatio(1,1)
                .start(HomeActivity.this);
    }

    public void addFragment(Fragment fragment, boolean addToBackStack, String tag) {
        currentFrame = tag;
        bottomFrag.setVisibility(View.GONE);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.addToBackStack(null);
        ft.replace(R.id.frameLayout, fragment, tag);
        ft.commitAllowingStateLoss();

    }

    @Override
    public void onBackPressed() {

        if (currentFrame.equals("nobottomnav")){
            bottomFrag.setVisibility(View.VISIBLE);
            currentFrame = "";
        }

        super.onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            myUrl = getPathFromURI(HomeActivity.this, imageUri);
            addFragment(new EditProfileFragment(), true, "nobottomnav");

        } else {
            Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show();
        }

    }

    public static String getPathFromURI(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

}
