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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.auth.User;
import com.google.rpc.context.AttributeContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class SendMoney extends AppCompatActivity {
    private ProgressDialog progressDialog;

    private double recieverAccount ;
    private double senderAccount ;
    private double amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_money);

        View mainView = findViewById(R.id.sendMoneyActivity);
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
    }

    public void error(String message){
        Toasty.error(this, message, Toast.LENGTH_LONG, true).show();
    }

    public void sendSucces(String message){
        Toasty.success(this, message, Toast.LENGTH_LONG, true).show();
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
    public void sendMoney(View v){

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
        String accountType="account1";

        double amountSending = Double.parseDouble(amount);
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String userEmail = firebaseUser.getEmail();

        if(email.isEmpty()==false){
            if(amount.isEmpty()==false){
                if(email.equals(userEmail.toString())){
                    progressDialog.dismiss();
                    Toasty.info(this, "You need to send for another user", Toast.LENGTH_LONG, true).show();
                }else{
                    firebaseAuth.getInstance().fetchSignInMethodsForEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                                @Override
                                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {


                                    if (task.isSuccessful()) {

                                        List signInMethods = task.getResult().getSignInMethods();
                                        if (signInMethods != null && !signInMethods.isEmpty()) {

                                            DocumentReference senderuserRef = FirebaseFirestore.getInstance()
                                                    .collection("users")
                                                    .document(userEmail);
                                            senderuserRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot document = task.getResult();
                                                        if (document.exists()) {
                                                            senderAccount=document.getDouble(accountType);

                                                            DocumentReference recieveruserRef = FirebaseFirestore.getInstance()
                                                                    .collection("users")
                                                                    .document(email);

                                                            recieveruserRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                    if (task.isSuccessful()) {
                                                                        DocumentSnapshot document = task.getResult();
                                                                        if (document.exists()) {
                                                                            recieverAccount=document.getDouble(accountType);

                                                                            if(amountSending<=senderAccount){
                                                                                double totoalReceiving=recieverAccount+amountSending;

                                                                                double newBalance=senderAccount-amountSending;

                                                                                Map<String, Object> updateSender = new HashMap<>();
                                                                                updateSender.put("account1", newBalance);
                                                                                senderuserRef.update(updateSender);

                                                                                Map<String, Object> updateReciver = new HashMap<>();
                                                                                updateReciver.put("account1", totoalReceiving);
                                                                                recieveruserRef.update(updateReciver);


                                                                                e.setText("");
                                                                                a.setText("");
                                                                                hideKeyboard();
                                                                                progressDialog.dismiss();
                                                                                sendSucces(amount+" USD sent to\n"+email);
                                                                                EmailSending em1=new EmailSending();
                                                                                em1.sendEmail(email,"Money Received Successfully","Hello\nYou have received "+amount+" USD by "+userEmail+".\nYour current account balance is USD "+totoalReceiving);
                                                                                em1.sendEmail(userEmail.toString(),"Money Sent Successfully","Hello!\nYou have sent "+amount+" USD to "+email+".\nYour current account balance of "+accountType+" is USD "+newBalance);
                                                                                update();
                                                                            }else {
                                                                                e.setText("");
                                                                                a.setText("");
                                                                                hideKeyboard();
                                                                                progressDialog.dismiss();
                                                                                error("Insufficent Balance");
                                                                            }

                                                                        } else {
                                                                            progressDialog.dismiss();
                                                                            error("Error");
                                                                        }
                                                                    } else {
                                                                        progressDialog.dismiss();
                                                                        task.getException().toString();return;
                                                                    }
                                                                }
                                                            });
                                                        } else {
                                                            progressDialog.dismiss();
                                                            error("Error");
                                                        }
                                                    } else {
                                                        progressDialog.dismiss();
                                                        task.getException().toString();
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
                Toasty.info(this, "Enter amount to send", Toast.LENGTH_SHORT, true).show();
            }
        }else{
            progressDialog.dismiss();
            Toasty.info(this, "Email cannot be empty", Toast.LENGTH_SHORT, true).show();
        }

    }
}