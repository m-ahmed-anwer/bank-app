package com.example.futurebank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AccountActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationViewiew;
    private double amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);


        bottomNavigationViewiew = findViewById(R.id.bottomNavigator);
        bottomNavigationViewiew.setSelectedItemId(R.id.menuListCard);

        bottomNavigationViewiew.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menuListHome:
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.menuListCard:
                        return true;

                    case R.id.menuListSett:
                        startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
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
}