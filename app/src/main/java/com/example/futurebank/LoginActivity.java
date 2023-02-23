package com.example.futurebank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }



    public void signup(View v){
        Intent i = new Intent(this, SignupActivity.class);
        startActivity(i);
    }

    public void firebase(View v){
        Intent i = new Intent(this,HomeActivity.class);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String email= ((EditText)findViewById(R.id.emailLog)).getText().toString();
        String password= ((EditText)findViewById(R.id.passwordLog)).getText().toString();

        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        startActivity(i);
                    } else {
                        // Account creation failed
                    }
                }
            });

    }



}