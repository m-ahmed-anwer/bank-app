package com.example.futurebank;

import android.content.Context;
import android.content.SharedPreferences;



public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "my_shared_pref";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";


    private static SharedPreferences sharedPreferences = null;
    private static SharedPreferences.Editor editor = null;


    public static void init(Context context) {
        sharedPreferences =context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static void saveUser(String email, String password) {
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PASSWORD, password);
        editor.apply();
    }

    public static String getEmail() {
        return sharedPreferences.getString(KEY_EMAIL, null);
    }

    public static String getPassword() {
        return sharedPreferences.getString(KEY_PASSWORD, null);
    }

    public static void clear() {
        editor.remove(KEY_EMAIL).apply();
        editor.remove(KEY_PASSWORD).apply();
    }



}
