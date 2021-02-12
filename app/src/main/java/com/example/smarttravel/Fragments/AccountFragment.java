package com.example.smarttravel.Fragments;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.smarttravel.Activities.HomeActivity;
import com.example.smarttravel.Activities.AccountEmailChange;
import com.example.smarttravel.Activities.AccountNameChange;
import com.example.smarttravel.R;

public class AccountFragment extends Fragment {

    TextView accountName,accountEmail;
    Button editAccountName,editAccountEmail;
    String name,email;
    Tag tag;
    HomeActivity homeActivity;

    public TextView retName(){
        return editAccountName;
    }
    public TextView retEmail(){
        return editAccountEmail;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.account_fragment,container,false);
        accountName = root.findViewById(R.id.accountName);
        accountEmail = root.findViewById(R.id.accountEmail);
        editAccountName = root.findViewById(R.id.editAccountName);
        editAccountEmail = root.findViewById(R.id.editAccountEmail);
        homeActivity = (HomeActivity) getActivity();

        editAccountName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(homeActivity, AccountNameChange.class));
            }
        });

        editAccountEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(homeActivity, AccountEmailChange.class));
            }
        });





        return root;
    }
}
