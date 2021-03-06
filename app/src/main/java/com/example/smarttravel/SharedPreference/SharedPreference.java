package com.example.smarttravel.SharedPreference;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SharedPreference {

    private static final String USER_DETAILS = "userDetails";
    private static final String USER_NAME = "userName";
    private static final String USER_EMAIL = "userEmail";
    private static final String USER_ID = "userId";
    private static final String USER_PIC = "userPic";

    public static String getUserPic(Context context) {
        SharedPreferences SPUserDetails= context.getSharedPreferences(USER_DETAILS,MODE_PRIVATE);
        return SPUserDetails.getString(USER_PIC,"");
    }

    public static void setUserPic(Context context,String value) {
        SharedPreferences SPUserDetails = context.getSharedPreferences(USER_DETAILS, MODE_PRIVATE);
        SharedPreferences.Editor editor = SPUserDetails.edit();
        editor.putString(USER_PIC , value);
        editor.apply();
    }

    public static String getUserId(Context context) {
        SharedPreferences SPUserDetails= context.getSharedPreferences(USER_DETAILS,MODE_PRIVATE);
        return SPUserDetails.getString(USER_ID,"");
    }

    public static void setUserId(Context context,String value) {
        SharedPreferences SPUserDetails = context.getSharedPreferences(USER_DETAILS, MODE_PRIVATE);
        SharedPreferences.Editor editor = SPUserDetails.edit();
        editor.putString(USER_ID , value);
        editor.apply();
    }

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
