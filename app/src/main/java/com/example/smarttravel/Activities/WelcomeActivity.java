package com.example.smarttravel.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smarttravel.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class WelcomeActivity extends AppCompatActivity {

    private static final String TAG = "TAG";
    private static final int RC_SIGN_IN = 2;
    TextView email, google;
    TextView signIn;
    Intent intent;
    FirebaseAuth auth;
    ProgressDialog progressDialog;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        email = findViewById(R.id.email_btn);
        google = findViewById(R.id.google_btn);
        signIn = findViewById(R.id.signin_btn);
        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);

        google.setOnClickListener(view -> signIn());

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

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                e.printStackTrace();
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        progressDialog.setMessage("Signing up...");
        progressDialog.show();

        try {
            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
            auth.signInWithCredential(credential)
                    .addOnCompleteListener(task -> {

                        if (task.isSuccessful()){
                            FirebaseUser user = auth.getCurrentUser();
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

                            HashMap<String, Object> map = new HashMap<>();
                            map.put("userId", user.getUid());
                            map.put("username", account.getDisplayName());
                            map.put("userEmail", account.getEmail());
                            map.put("userPic", account.getPhotoUrl().toString());

                            reference.child(user.getUid()).setValue(map).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()){
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(WelcomeActivity.this, "Failed !", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "onComplete: Database update "+ task1.getException().getMessage());
                                }
                            });

                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(WelcomeActivity.this, "Failed !", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onComplete: Database update "+ task.getException().getMessage());
                        }

                    });

        } catch (Exception e){
            progressDialog.dismiss();
            Log.d(TAG, "submitData: "+e.getMessage());
            e.printStackTrace();
        }
    }


}