package com.example.futurebank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import es.dmoral.toasty.Toasty;

public class SettingsActivity extends AppCompatActivity {


    String versionName = BuildConfig.VERSION_NAME;
    BottomNavigationView bottomNavigationViewiew;
    private ProgressDialog progressDialog;
    private ProgressDialog progressDialog2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);


        TextView t1 = (findViewById(R.id.textView17));
        t1.setText(versionName);

        bottomNavigationViewiew = findViewById(R.id.bottomNavigator);
        bottomNavigationViewiew.setSelectedItemId(R.id.menuListSett);

        bottomNavigationViewiew.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menuListHome:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.menuListCard:
                        startActivity(new Intent(getApplicationContext(), AccountActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.menuListSett:
                        return true;
                }
                return false;
            }
        });



        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Getting things ready");
        progressDialog.setCancelable(false);
        progressDialog.show();

        progressDialog2 = new ProgressDialog(this);
        progressDialog2.setMessage("Getting things ready");
        progressDialog2.setCancelable(false);
        progressDialog2.show();



        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userId = null;
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            userId = user.getUid();
        }

        FirebaseApp.initializeApp(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference switchRef = database.getReference("users").child(userId).child("notification_state");
        DatabaseReference touchIdRef = database.getReference("users").child(userId).child("touchId_state");


        Switch notification = findViewById(R.id.switch2);
        Switch touchId = findViewById(R.id.switch1);

        ImageButton btn1=findViewById(R.id.imageButton5);


        switchRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean switchState = dataSnapshot.getValue(Boolean.class);
                if (switchState == null) {
                    switchState = false; // set default value
                    switchRef.setValue(false);
                }
                notification.setChecked(switchState);
                if (switchState) {
                    btn1.setImageResource(R.drawable.ic_notifications_onn);
                } else {
                    btn1.setImageResource(R.drawable.ic_notifications_off);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // error
                progressDialog.dismiss();
                Toasty.error(SettingsActivity.this, "Database read failed", Toast.LENGTH_SHORT).show();
            }
        });

        touchIdRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean switchState = dataSnapshot.getValue(Boolean.class);
                if (switchState == null) {
                    switchState = false; // set default value
                    touchIdRef.setValue(false);
                }
                touchId.setChecked(switchState);
                progressDialog2.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // error
                progressDialog2.dismiss();
                Toasty.error(SettingsActivity.this, "Database read failed", Toast.LENGTH_SHORT).show();
            }
        });


        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (notification.isChecked()) {
                    switchRef.setValue(true);
                    btn1.setImageResource(R.drawable.ic_notifications_onn);
                } else {
                    switchRef.setValue(false);
                    btn1.setImageResource(R.drawable.ic_notifications_off);
                }
            }
        });

        touchId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (touchId.isChecked()) {
                    touchIdRef.setValue(true);
                } else {
                    touchIdRef.setValue(false);
                }
            }
        });


    }



    public void signout(View v){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signOut();
        SharedPrefManager.clear();
        Toasty.info(this, "You have been Signed Out", Toast.LENGTH_SHORT, true).show();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }



}