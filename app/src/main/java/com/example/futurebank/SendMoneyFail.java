package com.example.futurebank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SendMoneyFail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_money_fail);

        TextView e = findViewById(R.id.email);
        TextView a = findViewById(R.id.amount);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            e.setText(extras.getString("EMAIL"));
            Double amount = Double.parseDouble(extras.getString("AMOUNT"));
            a.setText(String.format("%.2f", amount));
        }
    }

    public void goBack(View v) {
        Intent home = new Intent(this, HomeActivity.class);
        finishAffinity();
        startActivity(home);
    }

    public void retry(View v) {
        finish();
    }

}