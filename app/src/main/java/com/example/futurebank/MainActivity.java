package com.example.futurebank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPrefManager.init(getApplicationContext());

        String savedEmail = SharedPrefManager.getEmail();
        String savedPassword = SharedPrefManager.getPassword();

        if (savedEmail != null && savedPassword != null) {
            findViewById(R.id.button2).performClick();
        }

    }

    public void signIn(View v){
        Intent i = new Intent(this,LoginActivity.class);
        startActivity(i);
    }
    public void signUp(View v){
        Intent i = new Intent(this, SignupActivity.class);
        startActivity(i);
    }
}