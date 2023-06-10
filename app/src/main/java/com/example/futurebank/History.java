package com.example.futurebank;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class History extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        RecyclerView recyclerView=findViewById(R.id.recycleView);

        List<HistoryItem> item=new ArrayList<HistoryItem>();
        item.add(new HistoryItem("Money Sent to","LKR 100.00","afraanwer037@gmail.com",R.drawable.baseline_send));
        item.add(new HistoryItem("Money Sent to","LKR 200.00","afraanwer037@gmail.com",R.drawable.baseline_recieve));
        item.add(new HistoryItem("Money Recieved by ","LKR 300.00","afraanwer037@gmail.com",R.drawable.baseline_send));
        item.add(new HistoryItem("Money Sent ","LKR 400.00","afraanwer037@gmail.com",R.drawable.baseline_recieve));
        item.add(new HistoryItem("Money Sent ","LKR 500.00","afraanwer037@gmail.com",R.drawable.baseline_send));
        item.add(new HistoryItem("Money Sent ","LKR 600.00","afraanwer037@gmail.com",R.drawable.baseline_recieve));
        item.add(new HistoryItem("Money Sent ","LKR 600.00","afraanwer037@gmail.com",R.drawable.baseline_recieve));
        item.add(new HistoryItem("Money Sent ","LKR 600.00","afraanwer037@gmail.com",R.drawable.baseline_recieve));
        item.add(new HistoryItem("Money Sent ","LKR 600.00","afraanwer037@gmail.com",R.drawable.baseline_recieve));
        item.add(new HistoryItem("Money Sent ","LKR 600.00","afraanwer037@gmail.com",R.drawable.baseline_recieve));


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RecycleList(getApplicationContext(),item ));

    }

    public void back(View v){
        Intent i = new Intent(this,HomeActivity.class);
        startActivity(i);
    }


}