package com.example.futurebank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

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

    public void back(View v){
        Intent i = new Intent(this,LoginActivity.class);
        startActivity(i);
    }




    public static boolean isValidDOB(String dob) {



        return false;
    }

    public static boolean isValidNIC(String nicNumber) {
        String regexPattern = "^[0-9]{9}[vVxX]$";

        if (nicNumber.matches(regexPattern)) {

            return true;

        }else if(nicNumber.length() == 12){
            int birthYear = Integer.parseInt(nicNumber.substring(0, 4));
            int dayNumber = Integer.parseInt(nicNumber.substring(4, 7));
            int serialNumber = Integer.parseInt(nicNumber.substring(7, 11));
            int currentYear = Calendar.getInstance().get(Calendar.YEAR) % 10000;
            if (birthYear > currentYear) {
                return false;
            }
            if (dayNumber < 1 || dayNumber > 366) {
                return false;
            }
            if (serialNumber < 1 || serialNumber > 9999) {
                return false;
            }

            return true;
        }else{
            return false;
        }

    }


    public static boolean isValidPhoneNumber(String phoneNumber) {
        // Regular expression pattern for Sri Lankan phone numbers
        String regexPattern = "^(?:\\+?94|0)(?:[78]\\d{8}|1\\d{2}\\d{6})$";

        return phoneNumber.matches(regexPattern);
    }

    public void success(String message){
        Toasty.success(this, message, Toast.LENGTH_SHORT, true).show();
    }

    public void register(View v){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Get Things Ready.....");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String name =((EditText)findViewById(R.id.fullName)).getText().toString().trim();
        String dob = ((EditText)findViewById(R.id.dateOfBirth)).getText().toString().trim();
        String nic = ((EditText)findViewById(R.id.nicNum)).getText().toString().trim();
        String phoneNum=((EditText)findViewById(R.id.phoneNum)).getText().toString().trim();


        Intent i = new Intent(this,HomeActivity.class);

        if(name.isEmpty()==false){
            if(dob.isEmpty()==false){
                if(isValidNIC(nic)==true && nic.isEmpty()==false){
                    if(isValidPhoneNumber(phoneNum)==true && phoneNum.isEmpty()==false){
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        FirebaseUser user =auth.getCurrentUser();
                        if(user !=null){
                            String email = user.getEmail().toString();
                            DocumentReference userRef = FirebaseFirestore.getInstance()
                                    .collection("users")
                                    .document(email);

                            String photoUrl = "https://t4.ftcdn.net/jpg/02/29/75/83/360_F_229758328_7x8jwCwjtBMmC6rgFzLFhZoEpLobB6L8.jpg";
                            UserProfileChangeRequest profileUpdates =new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .setPhotoUri(Uri.parse(photoUrl))
                                    .build();
                            int phone = Integer.parseInt(phoneNum);



                            user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Map<String, Object> newData = new HashMap<>();
                                    newData.put("account1", 1000.0);
                                    newData.put("dateOfBirth", dob);
                                    newData.put("phoneNumber", phone);
                                    newData.put("nic", nic);

                                    Map<String, Object> updates = new HashMap<>();
                                    updates.put("isFirstTimeVerification", false);

                                    userRef.update(updates);

                                    userRef.set(newData, SetOptions.merge());

                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);  // Destroy previous activities and clear Activity stack
                                    progressDialog.dismiss();
                                    startActivity(i);
                                    success("Account Created Successfully");


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
                        Toasty.error(this, "Invalid Phone Number", Toast.LENGTH_SHORT, true).show();
                    }
                }else{
                    progressDialog.dismiss();
                    Toasty.error(this, "Invalid NIC format", Toast.LENGTH_SHORT, true).show();
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