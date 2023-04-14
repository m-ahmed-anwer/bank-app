package com.example.futurebank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import es.dmoral.toasty.Toasty;

public class SettingsActivity extends AppCompatActivity {


    String versionName = BuildConfig.VERSION_NAME;
    BottomNavigationView bottomNavigationViewiew;
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
                switch (item.getItemId()){
                    case R.id.menuListHome:
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.menuListCard:
                        startActivity(new Intent(getApplicationContext(),AccountActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.menuListSett:
                        return true;
                }
                return false;
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