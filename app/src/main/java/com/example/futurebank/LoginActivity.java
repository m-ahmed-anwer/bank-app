package com.example.futurebank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {

    String email=null;
    String password=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

    }

    public void back(View v){
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }

    public void verify(){
        Toasty.info(this, "Please verify your account, Verification link send to "+email, Toast.LENGTH_LONG, true).show();
    }
    public void error(String message){
        Toasty.error(this, message, Toast.LENGTH_LONG, true).show();
    }

    public void signup(View v){
        Intent i = new Intent(this, SignupActivity.class);
        startActivity(i);
    }

    public void firebase(View v){
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        email =((EditText)findViewById(R.id.emailLog)).getText().toString();
        password= ((EditText)findViewById(R.id.passwordLog)).getText().toString();
        Intent i = new Intent(this,HomeActivity.class);
        auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            if(user.isEmailVerified()){
                                progressBar.setVisibility(View.GONE);
                                startActivity(i);
                            }else {
                                progressBar.setVisibility(View.GONE);
                                verify();
                            }
                        } else {
                            progressBar.setVisibility(View.GONE);
                            error(task.getException().getMessage());
                        }
                    }
                });
    }


}