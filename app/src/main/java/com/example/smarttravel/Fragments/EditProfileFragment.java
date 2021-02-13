package com.example.smarttravel.Fragments;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.smarttravel.Activities.HomeActivity;
import com.example.smarttravel.Models.User;
import com.example.smarttravel.R;
import com.example.smarttravel.SharedPreference.SharedPreference;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.gson.Gson;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileFragment extends Fragment {

    private static final String TAG = "TAG";
    EditText username;
    ImageView back;
    HomeActivity homeActivity;
    TextView save, editPic;
    ProgressDialog progressDialog;
    CircleImageView profilePic;
    StorageTask uploadTask;
    StorageReference storageReference;
    String myUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        username = root.findViewById(R.id.username_txt);
        back = root.findViewById(R.id.back_btn);
        homeActivity = (HomeActivity) getActivity();
        save = root.findViewById(R.id.save_btn);
        editPic = root.findViewById(R.id.edit_pic);
        profilePic = root.findViewById(R.id.profile_image);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        storageReference = FirebaseStorage.getInstance().getReference("posts");
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (homeActivity.imageUri != null) profilePic.setImageURI(homeActivity.imageUri);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        username.setText(SharedPreference.getUserName(getContext()));

        back.setOnClickListener(view1 -> homeActivity.onBackPressed());

        if (homeActivity.imageUri != null) profilePic.setImageURI(homeActivity.imageUri);

        save.setOnClickListener(view1 -> {
            if (username.getText().toString().trim().length() > 0){
                updateUser(username.getText().toString().trim());
            }
            else Toast.makeText(homeActivity, "Username cannot be empty", Toast.LENGTH_SHORT).show();
        });

        editPic.setOnClickListener(view1 -> homeActivity.addPicture());

        if (!SharedPreference.getUserPic(getContext()).equals("default")){
            Glide.with(getContext()).load(SharedPreference.getUserPic(getContext())).into(profilePic);
        }
    }


    private void updateUser(String s) {
        progressDialog.setMessage("Saving...");
        progressDialog.show();

        try {


            if (homeActivity.imageUri != null) {

                final StorageReference filereference = storageReference.child(System.currentTimeMillis()
                        +"."+homeActivity.myUrl);

                Log.d(TAG, "updateUser: File reference "+filereference);

                uploadTask = filereference.putFile(homeActivity.imageUri);
                uploadTask.continueWithTask(task -> {
                    if (!task.isSuccessful()){
                        progressDialog.dismiss();
                        throw task.getException();
                    }

                    return filereference.getDownloadUrl();
                }).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Uri downloadUri = (Uri) task.getResult();
                        myUrl = downloadUri.toString();

                        Gson gson = new Gson();
                        String taskResult = gson.toJson(task.getResult());

                        Log.d(TAG, "updateUser1: "+taskResult);
                        Log.d(TAG, "updateUser2: "+myUrl);

                        User user = new User(
                                SharedPreference.getUserId(getContext()),
                                s,
                                SharedPreference.getUserEmail(getContext()),
                                myUrl
                        );

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

                        reference.child(SharedPreference.getUserId(getContext())).setValue(user)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()){
                                        progressDialog.dismiss();
                                        Toast.makeText(homeActivity, "Saved successfully!", Toast.LENGTH_SHORT).show();
                                        homeActivity.updateSharedPreference();
                                        homeActivity.onBackPressed();
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(homeActivity, "Failed!", Toast.LENGTH_SHORT).show();
                                    }
                                });


                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(homeActivity, "Failed! "+task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });

            }


        } catch (Exception e){
            progressDialog.dismiss();
            Toast.makeText(homeActivity, "Failed : "+e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }
}