package com.example.futurebank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class ForgetPassword extends AppCompatActivity {
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
    }

    public void back(View v){
        startActivity(new Intent(this,LoginActivity.class));
    }

    public void verify(){
        Toasty.info(this, "Please check your email, Password reset email sent to "+email, Toast.LENGTH_LONG, true).show();
    }
    public void error(String message){
        Toasty.error(this, message, Toast.LENGTH_LONG, true).show();
    }

    public void forget(View v){
        email =((EditText)findViewById(R.id.emailForget)).getText().toString();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<String> signInMethods = task.getResult().getSignInMethods();
                        if (signInMethods != null && signInMethods.size() > 0) {
                            auth.sendPasswordResetEmail(email);
                            verify();
                            startActivity(new Intent(this,LoginActivity.class));
                        } else {
                            error("No user found in this Email");
                        }
                    } else {
                        error(task.getException().getMessage());
                    }
                });
    }

}