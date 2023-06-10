package com.example.futurebank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;
import java.text.SimpleDateFormat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

public class HomeActivity extends AppCompatActivity {


    private double amount;
    BottomNavigationView bottomNavigationViewiew;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    DatabaseReference myRef = database.getReference("message");


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        int edgeSize = ViewConfiguration.get(this).getScaledEdgeSlop();
        if (event.getAction() == MotionEvent.ACTION_DOWN && x < edgeSize && y > getActionBar().getHeight()) {
            // Touch event is near the left edge of the screen, but not close enough to trigger the back gesture
            return true;
        }
        return super.onTouchEvent(event);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        String date="";
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMM");
        String dateString = dateFormat.format(currentDate);
         date=dateString.toString();



        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userEmail = null;
        FirebaseUser user = mAuth.getCurrentUser();


        userEmail=user.getEmail().toString();
        String photoUrl = user.getPhotoUrl().toString();

        ImageView imageView = findViewById(R.id.imageView6);
        imageView.setBackgroundResource(R.drawable.image_background);
        Glide.with(this)
                .load(photoUrl)
                .into(imageView);


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

        startActivity(new Intent(getApplicationContext(),SendMoney.class));
    }
    public void showrecieve(View v){

        startActivity(new Intent(getApplicationContext(),RecieveMoney.class));
    }
    public void showhistory(View v){
        startActivity(new Intent(getApplicationContext(),History.class));
    }
    public void showrate(View v){
        startActivity(new Intent(getApplicationContext(), Exchangerate.class));
    }
}