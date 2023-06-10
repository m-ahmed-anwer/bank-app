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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.io.IOException;
import java.util.List;


import es.dmoral.toasty.Toasty;

public class RecieveMoney extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private double amount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recieve_money);

        View mainView = findViewById(R.id.scrollView2);
        mainView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    hideKeyboard();
                }
                return false;
            }
        });
        update();

    }
    public void update(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userEmail = null;
        FirebaseUser user = mAuth.getCurrentUser();
        userEmail=user.getEmail().toString();

        TextView accountText = findViewById(R.id.textView13);
        DocumentReference senderuserRef = FirebaseFirestore.getInstance()
                .collection("users")
                .document(userEmail);
        senderuserRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        amount = document.getDouble("account1");
                        accountText.setText("LKR "+String.format("%.2f", amount));
                    }
                }
            }
        });
    }

    public void back(View v){
        Intent i = new Intent(this,HomeActivity.class);
        startActivity(i);
    }
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(LoginActivity.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        view.clearFocus();
    }

    public void error(String message){
        Toasty.error(this, message, Toast.LENGTH_LONG, true).show();
    }


    public void sendSucces(String message){
        Toasty.success(this, message, Toast.LENGTH_SHORT, true).show();
    }


    public void sendRequest(View v){

        EditText e = findViewById(R.id.email);
        EditText a = findViewById(R.id.amount);

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Processing");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String email =e.getText().toString().trim();
        String amount =a.getText().toString().trim();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String userEmail = firebaseUser.getEmail();


        if(email.isEmpty()==false){
            if(amount.isEmpty()==false){
                if(email.equals(userEmail.toString())){
                    progressDialog.dismiss();
                    Toasty.info(this, "You need to request from another user", Toast.LENGTH_LONG, true).show();
                }else{
                    firebaseAuth.getInstance().fetchSignInMethodsForEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                                @Override
                                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                    if (task.isSuccessful()) {
                                        List signInMethods = task.getResult().getSignInMethods();
                                        if (signInMethods != null && !signInMethods.isEmpty()) {

                                            DocumentReference userRef = FirebaseFirestore.getInstance()
                                                    .collection("users")
                                                    .document(email);

                                            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot document = task.getResult();
                                                        if (document.exists()) {
                                                            double accountBalance = document.getDouble("account1");

                                                            String body="Hello!\nLKR"+amount+" money is requested by "+userEmail+"\nYou current account balance is LKR "+accountBalance;

                                                            EmailSending emailSend=new EmailSending();
                                                            emailSend.sendEmail(email,"Money Requested",body);

                                                            e.setText("");
                                                            a.setText("");
                                                            hideKeyboard();
                                                            progressDialog.dismiss();
                                                            sendSucces("Request sent to\n"+email);
                                                        }else {
                                                            progressDialog.dismiss();
                                                            error("No data");
                                                        }
                                                    } else {
                                                        progressDialog.dismiss();
                                                        error(task.getException().toString());
                                                    }

                                                }
                                            });


                                        } else {
                                            progressDialog.dismiss();
                                            error("User doesn't exists");
                                        }
                                    } else {
                                        progressDialog.dismiss();
                                        error(task.getException().toString());
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    error(e.toString());
                                }
                            });

                }

            }else{
                progressDialog.dismiss();
                Toasty.info(this, "Enter amount to request", Toast.LENGTH_SHORT, true).show();
            }
        }else{
            progressDialog.dismiss();
            Toasty.info(this, "Email cannot be empty", Toast.LENGTH_SHORT, true).show();
        }

    }
}