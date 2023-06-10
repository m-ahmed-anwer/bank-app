package com.example.futurebank;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecycleList extends RecyclerView.Adapter<ViewHolder>{

    Context context;
    List<HistoryItem> items;

    public RecycleList(Context context, List<HistoryItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.history,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.amountView.setText(items.get(position).getAccount());
        holder.imageButton.setImageResource(items.get(position).getImage());
        holder.detailView.setText(items.get(position).getDetail());
        holder.emailView.setText(items.get(position).getEmail());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
