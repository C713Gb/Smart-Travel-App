package com.example.smarttravel.Activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smarttravel.Fragments.AccountFragment;
import com.example.smarttravel.R;

public class AccountNameChange extends AppCompatActivity {



    EditText editNameChange;
    String s;
    Button back,save;
    AccountFragment accountFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_name_change);
        save=findViewById(R.id.saveNameEdit);
        back = findViewById(R.id.cancelNameEdit);
        editNameChange = findViewById(R.id.nameChangeEditText);

        save.setOnClickListener(view -> {
            s=editNameChange.getText().toString();
            //accountFragment.retName().setText(s);
            onBackPressed();
        });

        back.setOnClickListener(view -> onBackPressed());

    }
}