package com.example.futurebank;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class SendMoney extends AppCompatActivity {
    private ProgressDialog progressDialog;

    private double receiverAccount;
    private double senderAccount;
    private double amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_money);

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

    public void back(View v) {
        finish();
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

    public void error(String message) {
        Toasty.error(this, message, Toast.LENGTH_LONG, true).show();
    }

    public void sendSuccess(String message) {
        Toasty.success(this, message, Toast.LENGTH_LONG, true).show();
    }


    public void update() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userEmail = null;
        FirebaseUser user = mAuth.getCurrentUser();
        userEmail = user.getEmail().toString();

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
                        accountText.setText("LKR " + String.format("%.2f", amount));
                    }
                }
            }
        });

    }

    public void sendMoney(View v) {

        EditText e = findViewById(R.id.email);
        EditText a = findViewById(R.id.amount);

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Processing");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String email = e.getText().toString().trim();
        String amount = a.getText().toString().trim();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String accountType = "account1";

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String userEmail = firebaseUser.getEmail();

        if (email.isEmpty()) {
            progressDialog.dismiss();
            Toasty.info(this, "Email cannot be empty", Toast.LENGTH_SHORT, true).show();
            return;
        }
        if (amount.isEmpty()) {
            progressDialog.dismiss();
            Toasty.info(this, "Enter amount to send", Toast.LENGTH_SHORT, true).show();
            return;
        }
        if (email.equals(userEmail)) {
            progressDialog.dismiss();
            Toasty.info(this, "You need to send for another user", Toast.LENGTH_LONG, true).show();
            return;
        }

        double amountSending = Double.parseDouble(amount);

        // Check if account balance sufficient
        DocumentReference sendingUser = FirebaseFirestore.getInstance()
                .collection("users")
                .document(userEmail);
        sendingUser.get().addOnCompleteListener((sendUserData) -> {
            if (!sendUserData.isSuccessful() || !sendUserData.getResult().contains(accountType)) {
                if (sendUserData.getException() != null) {
                    Log.e("ERROR", sendUserData.getException().getMessage(), sendUserData.getException());
                }
                progressDialog.dismiss();
                error("Internal error occurred. Try again later");
                return;
            }
            if (sendUserData.getResult().getDouble(accountType) < amountSending) {
                progressDialog.dismiss();
                error("You do not have enough funds to perform this transaction.");
                return;
            }

            // Check if receive account exist and valid
            DocumentReference receiveUser = FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(email);
            receiveUser.get().addOnCompleteListener(receiveUserData -> {
                if (!receiveUserData.isSuccessful()) {
                    if (receiveUserData.getException() != null) {
                        Log.e("ERROR", receiveUserData.getException().getMessage(), receiveUserData.getException());
                    }
                    progressDialog.dismiss();
                    error("Internal error occurred. Try again later");
                    return;
                }
                if (!receiveUserData.getResult().contains(accountType)) {
                    progressDialog.dismiss();
                    error("No account found for the given email.");
                    return;
                }

                // perform the fund transfer
                receiverAccount = receiveUserData.getResult().getDouble(accountType);
                double totalReceiving = receiverAccount + amountSending;
                senderAccount = sendUserData.getResult().getDouble(accountType);
                double newBalance = senderAccount - amountSending;

                Map<String, Object> updateReciver = new HashMap<>();
                updateReciver.put("account1", totalReceiving);
                receiveUser.update(updateReciver).addOnCompleteListener((receiveStatus) -> {
                    if (receiveStatus.isSuccessful()) {
                        Map<String, Object> updateSender = new HashMap<>();
                        updateSender.put("account1", newBalance);
                        sendingUser.update(updateSender).addOnCompleteListener((sendStatus) -> {
                            if (sendStatus.isSuccessful()) {
                                e.setText("");
                                a.setText("");
                                hideKeyboard();
                                progressDialog.dismiss();
                                sendSuccess(amount + " LKR sent to\n" + email);
                                EmailSending em1 = new EmailSending();
                                em1.sendEmail(email,"Money Received","Hello\nYou have received LKR " + amount + " by " + userEmail + ".\nYour current account balance is LKR " + totalReceiving);
                                em1.sendEmail(userEmail, "Money Sent Successfully", "Hello!\nYou have sent " + amount + " LKR to " + email + ".\nYour current account balance of " + accountType + " is LKR " + newBalance);
                                update();
                                progressDialog.dismiss();
                            } else {
                                error("Transfer failed.");
                                updateReciver.clear();
                                updateReciver.put("account1", receiverAccount);
                                receiveUser.update(updateReciver);
                                progressDialog.dismiss();
                            }
                        });
                    } else {
                        error("Transfer failed.");
                        progressDialog.dismiss();
                    }
                });
            });
        });
    }
}