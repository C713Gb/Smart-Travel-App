package com.example.smarttravel.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.smarttravel.R;

public class WelcomeActivity extends AppCompatActivity {

    Button email, google;
    TextView signIn;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        email = findViewById(R.id.email_btn);
        google = findViewById(R.id.google_btn);
        signIn = findViewById(R.id.signin_btn);

        email.setOnClickListener(view -> {
            intent = new Intent(WelcomeActivity.this, UserDetailsActivity.class);
            intent.putExtra("signup", "true");
            startActivity(intent);
        });

        signIn.setOnClickListener(view -> {
            intent = new Intent(WelcomeActivity.this, UserDetailsActivity.class);
            intent.putExtra("signup", "false");
            startActivity(intent);
        });
    }
}