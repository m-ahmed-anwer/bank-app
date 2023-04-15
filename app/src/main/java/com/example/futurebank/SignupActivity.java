package com.example.futurebank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import es.dmoral.toasty.Toasty;

public class SignupActivity extends AppCompatActivity {

    String email=null;
    String password=null;
    String confirmPass=null;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        View mainView = findViewById(R.id.mainIdSignup);
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
        InputMethodManager imm = (InputMethodManager) getSystemService(SignupActivity.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void back(View v){
        startActivity( new Intent(this,MainActivity.class));
    }

    public void verify(){
        Toasty.success(this, "Email verification link send to "+email, Toast.LENGTH_LONG, true).show();
    }
    public void error(String message){
        Toasty.error(this, message, Toast.LENGTH_LONG, true).show();
    }
    public void login(View v){
        startActivity(new Intent(this,LoginActivity.class));
    }
    public void firebase(View v){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Setting things up...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        email= ((EditText)findViewById(R.id.emailSign)).getText().toString().trim();
        password= ((EditText)findViewById(R.id.passwordSign)).getText().toString().trim();
        confirmPass= ((EditText)findViewById(R.id.confirmSign)).getText().toString().trim();

        Intent i = new Intent(this,LoginActivity.class);

        if(email.isEmpty()==false){
            if(password.isEmpty()==false){
                if(password.equals(confirmPass)){

                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    auth.createUserWithEmailAndPassword(email,password)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user =auth.getCurrentUser();
                                        if(user !=null){
                                            String photoUrl = "https://t4.ftcdn.net/jpg/02/29/75/83/360_F_229758328_7x8jwCwjtBMmC6rgFzLFhZoEpLobB6L8.jpg";
                                            UserProfileChangeRequest profileUpdates =new UserProfileChangeRequest.Builder()
                                                    .setDisplayName("User")
                                                    .setPhotoUri(Uri.parse(photoUrl))
                                                    .build();

                                            user.updateProfile(profileUpdates)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            user.sendEmailVerification()
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()) {
                                                                                progressDialog.dismiss();
                                                                                verify();
                                                                                startActivity(i);
                                                                            } else {
                                                                                progressDialog.dismiss();
                                                                                error(task.getException().getMessage());
                                                                            }
                                                                        }
                                                                    });
                                                        } else {
                                                            progressDialog.dismiss();
                                                            error(task.getException().getMessage());
                                                        }
                                                    }
                                                });
                                        }
                                    } else {
                                        progressDialog.dismiss();
                                        error(task.getException().getMessage());
                                    }
                                }
                            });

                }else {
                    progressDialog.dismiss();
                    Toasty.error(this, "Confirm password doesn't match", Toast.LENGTH_SHORT, true).show();
                }
            }else {
                progressDialog.dismiss();
                Toasty.error(this, "Password Cannot be empty", Toast.LENGTH_SHORT, true).show();
            }
        }else {
            progressDialog.dismiss();
            Toasty.error(this, "Email Cannot be empty", Toast.LENGTH_SHORT, true).show();
        }




    }
}