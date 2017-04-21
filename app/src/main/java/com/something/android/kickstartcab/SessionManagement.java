package com.something.android.kickstartcab;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Anurag10 on 1/31/2017.
 */

public class SessionManagement {
    private static SessionManagement mInstance;
    private static Context mCtx;

    private static final String SHARED_PREF_NAME = "myPref";
    private  static final String KEY_Name = "name";
    private  static final String KEY_Email = "email";
    private  static final String KEY_Vehicle = "vehicle";
    private  static final String KEY_Id = "customerId";

    private SessionManagement(Context context) {
        mCtx = context;
    }

    public static synchronized SessionManagement getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SessionManagement(context);
        }
        return mInstance;
    }

    public boolean userLogin(String name, String emailId, String vehicleType,String customerId){

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Log.v("EditText","FailureBadly");
        Log.v("EditText",name);
        Log.v("EditText",emailId);
        Log.v("EditText",customerId);

        editor.putString(KEY_Name,name);
        editor.putString(KEY_Email,emailId);
        editor.putString(KEY_Vehicle,vehicleType);
        editor.putString(KEY_Id,customerId);
        editor.apply();

        return true;
    }

    public boolean isLoggedIn(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        if(sharedPreferences.getString(KEY_Email, null) != null){
            return true;
        }
        return false;
    }

    public boolean logout(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }


    public String getName(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_Name, null);
    }

    public String getEmail(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_Email, null);
    }

    public String getVehicle(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_Vehicle, null);
    }

    public String getCustomerId(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_Id, null);
    }
}