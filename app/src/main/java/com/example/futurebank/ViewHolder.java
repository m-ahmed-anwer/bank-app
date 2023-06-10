package com.example.futurebank;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {
    ImageButton imageButton;
    TextView amountView,detailView,emailView;


    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        imageButton=itemView.findViewById(R.id.imageButton4);
        amountView=itemView.findViewById(R.id.textView12);
        detailView=itemView.findViewById(R.id.textView11);
        emailView=itemView.findViewById(R.id.emailView);
    }
}
