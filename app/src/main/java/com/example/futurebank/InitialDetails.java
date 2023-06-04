package com.example.futurebank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import es.dmoral.toasty.Toasty;

public class InitialDetails extends AppCompatActivity {
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_details);



        View mainView = findViewById(R.id.initialActivity);
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
    public void error(String message){
        Toasty.error(this, message, Toast.LENGTH_LONG, true).show();
    }

    public void register(View v){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Get Things Ready.....");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String name =((EditText)findViewById(R.id.fullName)).getText().toString().trim();
        String dob = ((EditText)findViewById(R.id.dateOfBirth)).getText().toString().trim();
        String nic = ((EditText)findViewById(R.id.nicNum)).getText().toString().trim();
        String phone = ((EditText)findViewById(R.id.phoneNum)).getText().toString().trim();

        Intent i = new Intent(this,HomeActivity.class);

        if(name.isEmpty()==false){
            if(dob.isEmpty()==false){
                if(nic.isEmpty()==false){
                    if(phone.isEmpty()==false){
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        FirebaseUser user =auth.getCurrentUser();
                        if(user !=null){
                            DocumentReference userRef = FirebaseFirestore.getInstance()
                                    .collection("users")
                                    .document(user.getUid());

                            String photoUrl = "https://t4.ftcdn.net/jpg/02/29/75/83/360_F_229758328_7x8jwCwjtBMmC6rgFzLFhZoEpLobB6L8.jpg";
                            UserProfileChangeRequest profileUpdates =new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .setPhotoUri(Uri.parse(photoUrl))
                                    .build();

                            userRef.set(new HashMap() {{
                                put("dateOfBirth", dob);
                                put("phoneNumber", phone);
                                put("nic", nic);
                            }});
                            user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    userRef.update(new HashMap(){{
                                        put("isFirstTimeVerification", false);
                                    }});
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);  // Destroy previous activities and clear Activity stack
                                    progressDialog.dismiss();
                                    startActivity(i);
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
                        Toasty.error(this, "Phone Number cannot be empty", Toast.LENGTH_SHORT, true).show();
                    }
                }else{
                    progressDialog.dismiss();
                    Toasty.error(this, "NIC cannot be empty", Toast.LENGTH_SHORT, true).show();
                }
            }else{
                progressDialog.dismiss();
                Toasty.error(this, "Date of Birth cannot be empty", Toast.LENGTH_SHORT, true).show();
            }
        }else{
            progressDialog.dismiss();
            Toasty.error(this, "Name cannot be empty", Toast.LENGTH_SHORT, true).show();
        }
    }
}