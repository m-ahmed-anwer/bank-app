package com.example.futurebank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;
import java.text.SimpleDateFormat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class HomeActivity extends AppCompatActivity {


    BottomNavigationView bottomNavigationViewiew;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        String date="";
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMM");
        String dateString = dateFormat.format(currentDate);
         date=dateString.toString();


        FirebaseMessaging.getInstance().subscribeToTopic("Future Bank request Ahmed")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Done";
                        if (!task.isSuccessful()) {
                            msg = "Failed";
                        }

                    }
                });


        ((TextView)findViewById(R.id.dateView)).setText(date);

        bottomNavigationViewiew = findViewById(R.id.bottomNavigator);
        bottomNavigationViewiew.setSelectedItemId(R.id.menuListHome);

        bottomNavigationViewiew.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menuListHome:
                        return true;

                    case R.id.menuListCard:
                        startActivity(new Intent(getApplicationContext(),AccountActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.menuListSett:
                        startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });


    }

    public void showsend(View v){
        myRef.setValue("Edit");
    }
    public void showrecieve(View v){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                ((TextView)findViewById(R.id.textView8)).setText(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("","Failed");
            }
        });
    }
    public void showhistory(View v){
        startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
    }
    public void showrate(View v){
        startActivity(new Intent(getApplicationContext(), Exchangerate.class));
    }
}