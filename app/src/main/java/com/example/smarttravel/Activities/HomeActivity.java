package com.example.smarttravel.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.smarttravel.Fragments.AccountFragment;
import com.example.smarttravel.Fragments.ExploreFragment;
import com.example.smarttravel.Fragments.HomeFragment;
import com.example.smarttravel.Fragments.MusicFragment;
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

import timber.log.Timber;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "TAG";
    BottomNavigationView bottomFrag;
    DatabaseReference reference;
    FirebaseAuth auth;

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

    private void updateSharedPreference() {
        String userId = auth.getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference("users").child(userId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                SharedPreference.setUserEmail(HomeActivity.this, user.getUserEmail());
                SharedPreference.setUserId(HomeActivity.this, user.getUserId());
                SharedPreference.setUserName(HomeActivity.this, user.getUsername());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Timber.d("onCancelled: " + error.getMessage());
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
                    case R.id.bf_explore:
                        selectedFragment = new ExploreFragment();
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
}
