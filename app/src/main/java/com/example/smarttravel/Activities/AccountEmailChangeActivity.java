package com.example.smarttravel.Activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smarttravel.Fragments.AccountFragment;
import com.example.smarttravel.R;

public class AccountEmailChangeActivity extends AppCompatActivity {

    AccountFragment accountFragment;
    EditText editEmailChange;
    String s;
    Button back,save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_email_change);

        save=findViewById(R.id.saveEmailEdit);
        back = findViewById(R.id.cancelEmailEdit);
        editEmailChange = findViewById(R.id.nameChangeEditText);

        save.setOnClickListener(view -> {
            s=editEmailChange.getText().toString();
           // accountFragment.retEmail().setText(s);
            onBackPressed();
        });

        back.setOnClickListener(view -> onBackPressed());


    }
}