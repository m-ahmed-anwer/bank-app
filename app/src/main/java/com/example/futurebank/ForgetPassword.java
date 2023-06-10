package com.example.futurebank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
    String email=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        View mainView = findViewById(R.id.forgetPassword);
        mainView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    hideKeyboard();
                }
                return false;
            }
        });
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(ForgetPassword.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        view.clearFocus();
    }

    public void back(View v){
        startActivity(new Intent(this,LoginActivity.class));
    }

    public void error(String message){
        Toasty.error(this, message, Toast.LENGTH_SHORT, true).show();
    }



    public void forget(View v){
        email =((EditText)findViewById(R.id.emailForget)).getText().toString().trim();

        if(email.isEmpty()==false){
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.fetchSignInMethodsForEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            List<String> signInMethods = task.getResult().getSignInMethods();
                            if (signInMethods != null && signInMethods.size() > 0) {
                                auth.sendPasswordResetEmail(email);
                                Toasty.info(this, "Please check your email, Password reset email sent to "+email, Toast.LENGTH_LONG, true).show();
                                startActivity(new Intent(this,LoginActivity.class));
                            } else {
                                error("No user found in this Email");
                            }
                        } else {
                            error(task.getException().getMessage());
                        }
                    });
        }else {
            Toasty.error(this, "Email cannot be empty", Toast.LENGTH_SHORT, true).show();
        }

    }

}