package com.example.smarttravel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.smarttravel.fragments.AccountFragment;
import com.example.smarttravel.fragments.ExploreFragment;
import com.example.smarttravel.fragments.HomeFragment;
import com.example.smarttravel.fragments.MusicFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TAG";
    BottomNavigationView bottomFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomFrag = findViewById(R.id.bottom_fragment_menu);
        bottomFrag.setOnNavigationItemSelectedListener(bfragListner);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new HomeFragment()).commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener bfragListner =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, selectedFragment).commit();

                    return true;
                }
            };
}
