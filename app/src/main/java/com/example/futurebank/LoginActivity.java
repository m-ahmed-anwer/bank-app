package com.example.futurebank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {

    String email=null;
    String password=null;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPrefManager.init(getApplicationContext());


        EditText emailEditText = findViewById(R.id.emailLogin);
        EditText passwordEditText = findViewById(R.id.passwordLog);

        String savedEmail = SharedPrefManager.getEmail();
        String savedPassword = SharedPrefManager.getPassword();

        if (savedEmail != null && savedPassword != null) {
            emailEditText.setText(savedEmail);
            passwordEditText.setText(savedPassword);
            findViewById(R.id.constraintLayout8).performClick();
        }

        View mainView = findViewById(R.id.loginActivity);
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
        InputMethodManager imm = (InputMethodManager) getSystemService(LoginActivity.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading.....");
        progressDialog.setCancelable(false);
        progressDialog.show();


        FirebaseAuth auth = FirebaseAuth.getInstance();
        email =((EditText)findViewById(R.id.emailLogin)).getText().toString().trim();
        password= ((EditText)findViewById(R.id.passwordLog)).getText().toString().trim();


        Intent i = new Intent(this,HomeActivity.class);
        if(email.isEmpty()==false){
            if(password.isEmpty()==false){
                auth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = auth.getCurrentUser();
                                    if(user.isEmailVerified()){
                                        SharedPrefManager.saveUser(email, password);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);  // Destroy previous activities and clear Activity stack
                                        progressDialog.dismiss();
                                        startActivity(i);
                                    }else {
                                        progressDialog.dismiss();
                                        verify();
                                    }
                                } else {
                                    progressDialog.dismiss();
                                    error(task.getException().getMessage());
                                }
                            }
                        });
            }else {
                progressDialog.dismiss();
                Toasty.error(this, "Password cannot be empty", Toast.LENGTH_SHORT, true).show();

            }
        }else {
            progressDialog.dismiss();
            Toasty.error(this, "Email cannot be empty", Toast.LENGTH_SHORT, true).show();
        }

    }


    public void forget(View v){
        Intent i = new Intent(this,ForgetPassword.class);
        startActivity(i);
    }


}