package com.example.dmr.medicalrep.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public class SessionManager {

    public static final String CNIC="CNIC";
    public static final String FULL_NAME="Full Name";
    public static final String ADDRESS="Address";
    public static final String CITY="City";
    public static final String EMAIL="Email";
    public static final String PHONE_NUMBER="phoneNumber";

    public static void saveUser(@NonNull Context context, @NonNull Map<String,Object> map){
        SharedPreferences sharepref=context.getSharedPreferences("MyFile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =sharepref.edit();
        editor.putString(CNIC,map.get(CNIC).toString());
        editor.putString(FULL_NAME,map.get(FULL_NAME).toString());
        editor.putString(ADDRESS,map.get(ADDRESS).toString());
        editor.putString(CITY,map.get(CITY).toString());
        editor.putString(EMAIL,map.get(EMAIL).toString());
        editor.putString(PHONE_NUMBER,map.get(PHONE_NUMBER).toString());
        editor.apply();
    }

    @NonNull
    public static Map<String,Object> getUser(@NonNull Context context){
        SharedPreferences sharepref=context.getSharedPreferences("MyFile", Context.MODE_PRIVATE);
        Map<String,Object> map=new HashMap<>();
        map.put(FULL_NAME,sharepref.getString(FULL_NAME,""));
        map.put(CNIC,sharepref.getString(CNIC,""));
        map.put(ADDRESS,sharepref.getString(ADDRESS,""));
        map.put(CITY,sharepref.getString(CITY,""));
        map.put(EMAIL,sharepref.getString(EMAIL,""));
        map.put(PHONE_NUMBER,sharepref.getString(PHONE_NUMBER,""));
        return map;
    }

    public static void deleteUser(@NonNull Context context){
        SharedPreferences sharepref=context.getSharedPreferences("MyFile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =sharepref.edit();
        editor.putString(CNIC,"");
        editor.putString(FULL_NAME,"");
        editor.putString(ADDRESS,"");
        editor.putString(EMAIL,"");
        editor.putString(CITY,"");
        editor.putString(PHONE_NUMBER,"");
        editor.apply();
    }
}
