package com.example.futurebank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;

import java.util.Date;
import java.text.SimpleDateFormat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {


    BottomNavigationView bottomNavigationViewiew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        String date="";
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMM");
        String dateString = dateFormat.format(currentDate);
         date=dateString.toString();


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
        startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
    }
    public void showrecieve(View v){
        startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
    }
    public void showcard(View v){
        startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
    }
    public void showrate(View v){
        startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
    }
}