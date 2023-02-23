package com.example.futurebank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }

    public void login(View v){
        Intent i = new Intent(this,LoginActivity.class);
        startActivity(i);
    }
    public void home(View v){
        Intent i = new Intent(this,HomeActivity.class);
        startActivity(i);
    }
}