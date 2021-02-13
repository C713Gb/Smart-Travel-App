package com.example.smarttravel.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smarttravel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class UserDetailsActivity extends AppCompatActivity {

    private static final String TAG = "TAG";
    Intent intent;
    String signup = "", str_username = "", str_email = "", str_password = "";
    EditText username, email, password;
    TextView submit;
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        intent = getIntent();

        if (intent.getExtras() != null){
            signup = intent.getStringExtra("signup");
        }

        init();


        if (signup.length() > 0){
            if (signup.equals("true")){
                username.setVisibility(View.VISIBLE);
            }
            else {
                username.setVisibility(View.GONE);
            }
        }

        submit.setOnClickListener(view -> {
            if (signup.equals("true")) {
                if (validate()) submitData(str_email, str_username, str_password);
            } else {
                if (validate()) submitSignindata(str_email, str_password);
            }
        });

        back.setOnClickListener(view -> onBackPressed());

    }

    private void submitSignindata(String str_email, String str_password) {
        progressDialog.setMessage("Signing in...");
        progressDialog.show();

        try {
            auth.signInWithEmailAndPassword(str_email, str_password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            progressDialog.dismiss();
                            Intent intent = new Intent(UserDetailsActivity.this, HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(UserDetailsActivity.this, "Failed !", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onComplete: "+task.getException());
                            task.getException().printStackTrace();
                        }
                    });
        } catch (Exception e){
            progressDialog.dismiss();
            Log.d(TAG, "submitSignindata: "+e.getMessage());
            e.printStackTrace();
        }
    }

    private void init() {
        auth = FirebaseAuth.getInstance();
        username = findViewById(R.id.username_txt);
        email = findViewById(R.id.email_txt);
        password = findViewById(R.id.password_txt);
        submit = findViewById(R.id.submit_btn);
        back = findViewById(R.id.back_btn);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
    }

    private void submitData(String str_email, String str_username, String str_password) {
        progressDialog.setMessage("Signing up...");
        progressDialog.show();

        try {
            auth.createUserWithEmailAndPassword(str_email, str_password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()){

                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            String userId = firebaseUser.getUid();

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

                            HashMap<String, Object> map = new HashMap<>();
                            map.put("userId", userId);
                            map.put("username", str_username);
                            map.put("userEmail", str_email);
                            map.put("userPic", "default");

                            reference.child(userId).setValue(map).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()){
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(UserDetailsActivity.this, HomeActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(UserDetailsActivity.this, "Failed !", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "onComplete: Database update "+ task1.getException().getMessage());
                                }
                            });

                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(UserDetailsActivity.this, "Failed !", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onComplete: "+task.getException());
                            task.getException().printStackTrace();
                        }
                    });
        } catch (Exception e){
            progressDialog.dismiss();
            Log.d(TAG, "submitData: "+e.getMessage());
            e.printStackTrace();
        }


    }

    private boolean validate() {
        if (signup.equals("true")){
            if (username.getText().toString().length() == 0){
                Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
                return false;
            }
            str_username = username.getText().toString().trim();
        }

        if (email.getText().toString().length() == 0){
            Toast.makeText(this, "Email cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.getText().toString().length() < 5){
            Toast.makeText(this, "Password at least 6 digits", Toast.LENGTH_SHORT).show();
            return false;
        }

        str_password = password.getText().toString().trim();
        str_email = email.getText().toString().trim();

        return true;
    }
}