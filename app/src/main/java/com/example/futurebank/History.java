package com.example.futurebank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class History extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        RecyclerView recyclerView=findViewById(R.id.recycleView);

        List<HistoryItem> item=new ArrayList<HistoryItem>();


        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        DatabaseReference historyRef = usersRef.child(uid).child("history");

        TextView yourTextView=findViewById(R.id.textView40);
        historyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    item.clear();
                    yourTextView.setText(null);

                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        String account = childSnapshot.child("account").getValue(String.class).toString();
                        String email = childSnapshot.child("email").getValue(String.class).toString();
                        String detail = childSnapshot.child("detail").getValue(String.class).toString();
                        int image = childSnapshot.child("image").getValue(Integer.class);
                        item.add(new HistoryItem(detail,account,email,image));
                    }
                    recyclerView.setAdapter(new RecycleList(getApplicationContext(),item ));
                } else {

                    yourTextView.setText("Sorry, we couldn't find any history");
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Failed to retrieve history: " + error.getMessage().toString());
            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RecycleList(getApplicationContext(),item ));

    }

    public void send(String message){
        Toasty.info(this, message, Toast.LENGTH_SHORT, true).show();
    }
    public void back(View v){
        Intent i = new Intent(this,HomeActivity.class);
        startActivity(i);
    }


}