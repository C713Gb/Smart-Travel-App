package com.example.smarttravel.SharedPreference;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SharedPreference {

    private static final String USER_DETAILS = "userDetails";
    private static final String USER_NAME = "userName";
    private static final String USER_EMAIL = "userEmail";

    public static String getUserEmail(Context context) {
        SharedPreferences SPUserDetails= context.getSharedPreferences(USER_DETAILS,MODE_PRIVATE);
        return SPUserDetails.getString(USER_EMAIL,"");
    }

    public static void setUserEmail(Context context,String value) {
        SharedPreferences SPUserDetails = context.getSharedPreferences(USER_DETAILS, MODE_PRIVATE);
        SharedPreferences.Editor editor = SPUserDetails.edit();
        editor.putString(USER_EMAIL , value);
        editor.apply();
    }

    public static String getUserName(Context context) {
        SharedPreferences SPUserDetails= context.getSharedPreferences(USER_DETAILS,MODE_PRIVATE);
        return SPUserDetails.getString(USER_NAME,"");
    }

    public static void setUserName(Context context,String value) {
        SharedPreferences SPUserDetails = context.getSharedPreferences(USER_DETAILS, MODE_PRIVATE);
        SharedPreferences.Editor editor = SPUserDetails.edit();
        editor.putString(USER_NAME , value);
        editor.apply();
    }


}
